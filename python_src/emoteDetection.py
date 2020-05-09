from PIL import Image
import numpy as np
import cv2
import base64
import io
from confluent_kafka import Consumer, KafkaError, Producer
import json


DEFAULT_KAFKA_ADDRESS = "192.168.160.103:9093"

def stringToImage(base64_image_string):
    if("data:image" in base64_image_string):
        print("Re-writing base64 image string")
        base64_image_string = base64_image_string[base64_image_string.index("base64,") + len("base64,"):]
    try:
        imgdata = base64.b64decode(base64_image_string)
        img = Image.open(io.BytesIO(imgdata))
    except:
        print("Couldn't convert string to image")

    return cv2.cvtColor(np.array(img), cv2.COLOR_BGR2RGB)



def detect_emote(base64_image_string):


    image = stringToImage(base64_image_string)
    smileDetector = cv2.CascadeClassifier('models/HaarCascade.xml')
    smiles = smileDetector.detectMultiScale(image, scaleFactor = 1.8, minNeighbors = 20)

    # for (sx, sy, sw, sh) in smiles:
    #     cv2.rectangle(image, (sx, sy), ((sx + sw), (sy + sh)), (0, 255,0), 5)

    if(len(smiles) == 0):
        print("No smile detected!")
        return "Neutral"
    else:
        return "Happy"


def build_response(id, emote):
    d = {"id": id, "emote": emote}
    return json.dumps(d)

def onSuccess():
    print("Successfully sent emote!")




if __name__ == "__main__":
    
    # f = open("../../test_images/b64")
    # b64_test = f.readline()
    # print(b64_test[:250])

    settings = {
        'bootstrap.servers': DEFAULT_KAFKA_ADDRESS,
        'client.id': 'client-1',
        'group.id' : 'p30',
        'session.timeout.ms': 6000,
        'default.topic.config': {'auto.offset.reset': 'smallest'}
    }
    prod_settings = {'bootstrap.servers': DEFAULT_KAFKA_ADDRESS}

    c = Consumer(settings)

    c.subscribe(['p30-photos'])

    try:
        print("Emote detection module now listening!")
        while True:
            msg = c.poll(0.1)
            if msg is None:
                continue
            elif not msg.error():
                print('Received message: {0}'.format(msg.value()[:60]))
                try:
                    msg_object = json.loads(msg.value().decode("utf-8"))
                except Exception:
                    print("Wrong message format!")
                    continue

                msg_id = msg_object["id"]
                # print("image: ", msg_object["image"][:120])
                emote = detect_emote(msg_object["image"])
                # emote = detect_emote(b64_test)
                jsonObject = build_response(msg_id, emote)
                print(jsonObject)
                producer = Producer(**prod_settings)
                producer.produce("p30-classification", jsonObject)
            
            elif msg.error().code() == KafkaError._PARTITION_EOF:
                print('End of partition reached {0}/{1}'
                    .format(msg.topic(), msg.partition()))
            else:
                print('Error occured: {0}'.format(msg.error().str()))

    except KeyboardInterrupt:
        pass

    finally:
        print("Closing detection module.")
        c.close()