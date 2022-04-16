package com.example.restapi;

import com.example.restapi.board.entity.Board;
import com.example.restapi.board.repository.BoardRepository;
import com.example.restapi.file.properties.FileProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

@SpringBootApplication
@EnableConfigurationProperties(FileProperties.class)
public class RestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(BoardRepository boardRepository) {
		return (args) -> {
			IntStream.rangeClosed(1, 50).forEach(index ->
					boardRepository.save(Board.builder()
							.title(String.format("제목%s", index))
							.content(String.format("내용%s", index))
							.build())
			);
		};
	}
}
