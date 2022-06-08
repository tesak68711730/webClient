package com.example.demo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	TodoClient todoClient() throws Exception {
		WebClient webClient = WebClient.builder()
				.baseUrl("https://jsonplaceholder.typicode.com")
				.build();
		HttpServiceProxyFactory factory = new HttpServiceProxyFactory(new WebClientAdapter(webClient));
		factory.afterPropertiesSet();
		return factory.createClient(TodoClient.class);
	}

	record Todo(Long id, String title, Boolean completed, Long userId) {}

	@HttpExchange("/todos")
	interface TodoClient {
		@GetExchange
		List<Todo> todos();
		@PostExchange
		Todo createTodo(@RequestBody Todo request);
		@GetExchange("/{todoID}")
		ResponseEntity<Todo> getTodoByID(@PathVariable("todoID") Long id);
	}

	@Bean
	ApplicationRunner applicationRunner (TodoClient todoClient) {
		return args -> {
//			System.out.println(todoClient.todos());
//			Todo todo = new Todo(null, "asdasd", true, 1L);
//			Todo todo1 = null;
//			Todo response = todoClient.createTodo(todo);
//			System.out.println(todo1.id());
			System.out.println(todoClient.getTodoByID(1L).getBody());
		};
	}
}
