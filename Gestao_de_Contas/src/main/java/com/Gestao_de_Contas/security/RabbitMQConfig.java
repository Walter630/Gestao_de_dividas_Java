package com.Gestao_de_Contas.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter; // ✅ Import correto
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE    = "debt.notification.queue";
    public static final String EXCHANGE = "debt.notification.exchange";
    public static final String ROUTING  = "debt.notification";


    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING);
    }

    // ✅ Passa ObjectMapper explicitamente — resolve o deprecated
    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter(); // ✅
    }

    // ✅ Bean separado para envio de mensagens
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    // ✅ Bean separado para administração (criar filas/exchanges no broker)
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }



    //-----------------------------CLIENT_-------------------------
    public static final String CLIENTE_QUEUE = "cliente.notifications";
    public static final String CLIENTE_ROUTING = "cliente.routing";
    @Bean
    public Queue queueClient() {return new Queue(CLIENTE_QUEUE, true);}

    @Bean
    public Binding bindingClient(Queue queueClient, DirectExchange exchange) {
        return BindingBuilder.bind(queueClient).to(exchange).with(CLIENTE_ROUTING);
    }
}