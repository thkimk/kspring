package com.kkk.sbgtest.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.utils.Utils;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaPoller extends Thread {
    boolean isContinue = true;
    KafkaConsumer<String, Object> kafkaConsumer;

    public KafkaPoller(String threadName) {
        super.setName(threadName);
    }

    // KafkaConsumer.start() 호출시, run() 구동됨 (쓰레드 활성화)
    public void run() {
        Properties props = new Properties();
        props.put("max.poll.records", "200");

        kafkaConsumer = new KafkaConsumer<String, Object>(props);
        kafkaConsumer.subscribe(Arrays.asList("Topic-name"));

        // kafka 접속 후 3초 지난 후에 message subscribe 시작 함
        // 초기 시스템 부하를 줄이기 위함

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        while (isContinue) {
            ConsumerRecords<String, Object> records = null;

            try {
                records = kafkaConsumer.poll(Duration.ofMillis(3000));
            } catch (Exception ex) {
                // kafka에서 record를 읽어 오는 부분은 오류 발생 시
                // 아무 처리를 하지 않고 warning 메시지만 출력 후 다시 재처리 하도록 함.
            }

            // 오류 발생 카운터
            int retryCount = 0;

            // 오류 발생 시 retry 여부 플래그
            boolean bRetry = true;

            // 메시지가 정상적으로 읽혀 왔을 때만 처리
            // 메시지 처리 시 오류 날 경우 계속 retry 함
            // (계속 retry 하지 않을 경우 데이터정합성이 깨어짐)
            if (!records.isEmpty()) {
                while(bRetry) {
                    try {
                        process(records);
                        kafkaConsumer.commitSync();
                        bRetry = false;
                        retryCount = 0;
                    } catch (Exception ex) {
                        bRetry = true;

                        // circuit breaker 적용 (오류가 나면 처음엔 재시도 시간이 짧게 하고
                        // 오류 반복 횟수가 늘어 날 수록 재시도 타임 증가하도록 처리
                        retryCount = retryCount < 600 ? retryCount += 1 : 600;

                        for (int i=0; i<retryCount; i++) {
                            // thread 종료 명령이 들어오면 바로 루프를 빠져 나옴
                            if(!isContinue) {
                                bRetry = false;
                                break;
                            }
                            Utils.sleep(100);
                        }
                    }
                }
            }
        }
    }

    public void stopWorker() {
        this.isContinue = false;
    }

    public void process(ConsumerRecords<String, Object> records ) throws Exception {
        // 여기에 로직을 넣지 말고, 상속 받은 자식 클래스에 override해서 로직을 넣어야 함
    }
}
