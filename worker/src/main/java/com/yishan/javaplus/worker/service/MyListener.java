package com.yishan.javaplus.worker.service;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.stereotype.Service;import javax.jms.*;@Servicepublic class MyListener implements MessageListener {    private final Logger logger = LoggerFactory.getLogger(getClass());    @Override    public void onMessage(Message message) {        try {            logger.info("Received: " + ((TextMessage) message).getText());        } catch (JMSException e) {            e.printStackTrace();        }    }}