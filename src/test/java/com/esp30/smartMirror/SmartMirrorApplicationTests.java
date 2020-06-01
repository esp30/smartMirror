package com.esp30.smartMirror;

import com.esp30.smartMirror.data.Emotion;
import com.esp30.smartMirror.data.EmotionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmartMirrorApplicationTests {

	@Autowired
	private EmotionRepository emotionRepository;

	@Test
	void contextLoads() {
		Emotion emotionT = new Emotion("Tiago", "happy");
		Emotion emotionM = new Emotion("Mariana", "sad");
		Emotion emotionR = new Emotion("Raquel", "neutral");

		emotionRepository.save(emotionT);
		emotionRepository.save(emotionM);
		emotionRepository.save(emotionR);
	}

}
