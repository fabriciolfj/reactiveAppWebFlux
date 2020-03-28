package br.com.estudo.reactiveapp;

import br.com.estudo.reactiveapp.domain.model.Product;
import br.com.estudo.reactiveapp.domain.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.LinkedHashMap;

@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ReactiveAppApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private ProductService productService;

	@Value("${config.base.endpoint}")
	private String url;

	@Test
	public void deleteByIdTest() {
		var product = productService.findByNome("moto").block();
		client.delete()
				.uri(url + "/{id}", Collections.singletonMap("id", product.getId()))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNoContent()
				.expectBody()
				.isEmpty();

		client.get()
				.uri(url + "/{id}", Collections.singletonMap("id", product.getId()))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound()
				.expectBody()
				.isEmpty();
	}

	@Test
	public void updateTest(){
		var category = productService.findByNameCategory("Computação").block();
		var product = productService.findByNome("tv").block();
		var productEdited = new Product("tv2",  18.00, category);

		client.put().uri(url + "/{id}", Collections.singletonMap("id", product.getId()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(productEdited), Product.class)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(Product.class)
				.consumeWith(response -> {
					var prod = response.getResponseBody();
					Assertions.assertThat(prod.getCategory().getNome().equals(category.getNome())).isTrue();
					Assertions.assertThat(prod.getId()).isNotEmpty();
				});

	}

	@Test
	public void createTest(){
		var category = productService.findByNameCategory("Esport").block();
		var product = new Product("game", 19.99, category);
		client.post().uri(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(product), Product.class)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(new ParameterizedTypeReference<LinkedHashMap<String, Object>>() {})
		.consumeWith(response -> {
			var o = response.getResponseBody();
			var prod = new ObjectMapper().convertValue(o, Product.class);
			Assertions.assertThat(prod.getNome().equals(product.getNome())).isTrue();
			Assertions.assertThat(prod.getId()).isNotEmpty();
		});

	}

	@Test
	public void findByIdTest() {
		var product = productService.findByNome("caneca").block();
		client.get()
				.uri(url + "/{id}", Collections.singletonMap("id", product.getId()))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(Product.class)
				.consumeWith(response -> {
					var prod = response.getResponseBody();
					Assertions.assertThat(prod.getNome()).isEqualTo("caneca");
					Assertions.assertThat(prod.getId()).isNotEmpty();
					Assertions.assertThat(prod.getId().length() > 0).isTrue();
				});
				//.jsonPath("$.id").isNotEmpty()
				//.jsonPath("$.nome").isEqualTo("tv");
	}

	@Test
	public void listarTest(){
		client.get()
				.uri(url)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Product.class)
				.consumeWith(response -> {
					var products = response.getResponseBody();
					products.forEach(p -> {
						System.out.println(p.getNome());
					});

					Assertions.assertThat(products.size() > 0).isTrue();
				});
	//.hasSize(5);
	}

}
