package com.yishan.javaplus.config;import com.amazonaws.services.s3.AmazonS3;import com.amazonaws.services.sqs.AmazonSQS;import com.amazonaws.services.sqs.model.*;import org.mockito.Mockito;import org.springframework.beans.factory.annotation.Value;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import org.springframework.context.annotation.Profile;import java.util.List;@Configurationpublic class MockConfig {    @Value("${aws.queueName}")    private String queueName;    List<Message> messages;    private String queueUrl = "URL";//    @Bean//    public StorageService getStorageService(@Autowired AmazonS3 s3Client) throws IOException {//        StorageService storageService = new StorageService(s3Client);//        storageService.setBucket("unit_test");//        return storageService;//        }////    @Bean//    public MessageSQSService getMessageSQSService(@Autowired AmazonSQS sqsClient) throws IOException{//        MessageSQSService messageSQSService = new MessageSQSService(sqsClient, queueName);//        //messageSQSService.set("unit_test");//        return messageSQSService;//    }    @Bean    @Profile("unit")    public AmazonS3 getAmazonS3Client(){        AmazonS3 s3Client = Mockito.mock(AmazonS3.class);        return s3Client;    }    @Bean    @Profile("unit")    public AmazonSQS getAmazonSQSClient(){        AmazonSQS sqsClient = Mockito.mock(AmazonSQS.class);        GetQueueUrlResult getQueueUrlResult = Mockito.mock(GetQueueUrlResult.class);        Mockito.when(sqsClient.getQueueUrl(queueName)).thenReturn(getQueueUrlResult);        Mockito.when(getQueueUrlResult.getQueueUrl()).thenReturn("URL");        ReceiveMessageResult receiveMessageResult = Mockito.mock(ReceiveMessageResult.class);        Mockito.when(sqsClient.receiveMessage(queueUrl)).thenReturn(receiveMessageResult);        return sqsClient;    }}