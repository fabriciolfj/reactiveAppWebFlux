package br.com.estudo.reactiveapp;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import br.com.estudo.reactiveapp.domain.dao.ProductRepository;
import br.com.estudo.reactiveapp.domain.model.Category;
import br.com.estudo.reactiveapp.domain.model.Product;
import br.com.estudo.reactiveapp.domain.service.ProductService;
import io.micrometer.shaded.org.pcollections.PStack;
import reactor.core.publisher.Flux;

@SpringBootApplication
//@EnableReactiveMethodSecurity
public class ReactiveAppApplication implements CommandLineRunner {

	@Autowired
	private ProductService productService;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	private static  final Logger log = LoggerFactory.getLogger(ReactiveAppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ReactiveAppApplication.class, args);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(converter);

		return template;
	}

	@Bean
	public Jackson2JsonMessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}


	@Bean
	public RabbitListenerContainerFactory<SimpleMessageListenerContainer> prefetchOneContainerFactory(
			SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
		var factory = new SimpleRabbitListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		factory.setPrefetchCount(1);

		return factory;
	}

	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("products").subscribe();
		mongoTemplate.dropCollection("categorias").subscribe();

		Category eletronico = new Category(null, "Eletronico");
		Category esport = new Category(null, "Esport");
		Category computacao = new Category(null, "Computação");

		Flux.just(eletronico, esport, computacao).flatMap(productService::saveCategory)
		.doOnNext(c -> {
			log.info("Categoria criada: " + c.getNome());
		}).thenMany(Flux.just(
				new Product("tv", 29.00, eletronico),
				new Product("moto", 1999.00, esport),
				new Product("jogo", 87.00, eletronico),
				new Product("caneca", 9.00, computacao),
				new Product("toalha", 74.00, computacao)
		).flatMap(product -> {
			product.setCreateAt(new Date());
			return productService.save(product);
		}).doOnNext(product -> log.info("insert: " + product.getId() + " " + product.getNome()))).subscribe();
	}

	/*@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
		return http.formLogin()
				.and()
				.authorizeExchange()
				.anyExchange().authenticated()
				.and()
				.build();
	}

	@Bean
	public ReactiveUserDetailsService userDetailsService(){
		UserDetails user = User.withUsername("user").password("password").roles("USER", "ADMIN").build();
		return new MapReactiveUserDetailsService(user);
	}*/



}



