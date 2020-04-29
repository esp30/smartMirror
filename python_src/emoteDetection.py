from PIL import Image
# from matplotlib import pyplot as plt
import numpy as np
import face_recognition
import keras
from keras.models import load_model
import cv2
import base64
import io
from confluent_kafka import Consumer, KafkaError, Producer
import json


DEFAULT_KAFKA_ADDRESS = "192.168.160.103:9092"

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
    try:
        face_image = stringToImage(base64_image_string)
        face_image = cv2.resize(face_image, (48,48))
        face_image = cv2.cvtColor(face_image, cv2.COLOR_BGR2GRAY)
        face_image = np.reshape(face_image, [1, face_image.shape[0], face_image.shape[1], 1])
        
        predicted_class = np.argmax(model.predict(face_image))

        label_map = dict((v,k) for k,v in emotion_dict.items()) 
        predicted_label = label_map[predicted_class]
    except:
        print("Error while detecting an emote!")
        return

    return predicted_label

def build_response(id, emote):
    d = {"id": id, "emote": emote}
    return json.dumps(d)

def onSuccess():
    print("Successfully sent emote!")




if __name__ == "__main__":
    
    emotion_dict = {'Angry': 0, 'Sad': 5, 'Neutral': 4, 'Disgust': 1, 'Surprise': 6, 'Fear': 2, 'Happy': 3}
    model = load_model("models/model_v6_23.hdf5")
    
    b64_test = " data:image/jpeg;base64,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"
    
    settings = {
        'bootstrap.servers': DEFAULT_KAFKA_ADDRESS,
        'client.id': 'client-1',
        'group.id' : 'p30',
        'session.timeout.ms': 6000,
        'default.topic.config': {'auto.offset.reset': 'smallest'}
    }
    prod_settings = {'bootstrap.servers': DEFAULT_KAFKA_ADDRESS}

    c = Consumer(settings)

    c.subscribe(['p30-test-topic'])

    try:
        print("Emote detection module now listening!")
        while True:
            msg = c.poll(0.1)
            if msg is None:
                continue
            elif not msg.error():
                print('Received message: {0}'.format(msg.value()[:60]))
                msg_object = json.loads(msg.value().decode("utf-8"))
                msg_id = msg_object["id"]
                print("image: ", msg_object["image"][:120])
                emote = detect_emote(msg_object["image"])
                # emote = detect_emote(b64_test)
                
                jsonObject = build_response(msg_id, emote)
                print(jsonObject)
                producer = Producer(**prod_settings)
                producer.produce("p30-test-topic2", jsonObject)
            
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