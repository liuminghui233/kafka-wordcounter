import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyConsumer {
    
    static final String topic = "plaintext";
    static final String bootstrapServers = "localhost:9092";

    public static void main(final String[] args) throws Exception {
        // Consumer config
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-consumer-1");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    String filePath = record.key();
                    String textLines = record.value();
                    System.out.print(filePath);
                    System.out.printf("Consumed record with key %s and value:\n%s\nWordconuter result:\n", filePath, textLines);

                    // Init output file
                    File outputFile = new File(filePath + "-wordcount");
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

                    // Word counter
                    final Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);
                    List<String> wordList = Arrays.asList(pattern.split(textLines.toLowerCase()));
                    wordList.stream().collect(Collectors.groupingBy((x) -> x)).values().stream().map((x) -> {
                        HashMap<String, Integer> map = new HashMap<>();
                        map.put(x.get(0), x.size());
                        return map;
                    }).forEach(item->{
                        // Output to file
                        try {
                            bw.write(String.valueOf(item));
                            bw.newLine();
                            System.out.println(item);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    bw.close();
                }
            }
        } finally {
            consumer.close();
        }
    }

}
