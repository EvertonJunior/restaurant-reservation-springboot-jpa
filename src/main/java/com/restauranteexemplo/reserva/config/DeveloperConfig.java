package com.restauranteexemplo.reserva.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.restauranteexemplo.reserva.entities.Client;
import com.restauranteexemplo.reserva.entities.Reservation;
import com.restauranteexemplo.reserva.entities.Table;
import com.restauranteexemplo.reserva.repositories.ClientRepository;
import com.restauranteexemplo.reserva.repositories.ReservationRepository;
import com.restauranteexemplo.reserva.repositories.TableRepository;

@Configuration
@Profile("developer")
@EnableScheduling
public class DeveloperConfig implements CommandLineRunner {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private TableRepository tableRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Override
	public void run(String... args) throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

		Client client1 = new Client(null, "Jose Everton", "jose@gmail.com", "1234567");
		Client client2 = new Client(null, "Maria Eduarda", "maria@gmail.com", "87654321");
		Client client3 = new Client(null, "Renan Lodi", "renan@gmail.com", "1545534");

		clientRepository.saveAll(Arrays.asList(client1, client2, client3));

		Table table1 = new Table(null, 4);
		Table table2 = new Table(null, 3);
		Table table3 = new Table(null, 2);

		tableRepository.saveAll(Arrays.asList(table1, table2, table3));

		Reservation reservation1 = new Reservation(null, client1, LocalDateTime.parse("04/08/2024 10:00", formatter),
				LocalDateTime.parse("04/08/2024 18:20", formatter), table1);
		Reservation reservation2 = new Reservation(null, client2, LocalDateTime.parse("04/08/2024 20:25", formatter),
				LocalDateTime.parse("04/08/2024 22:30", formatter), table2);

		reservationRepository.saveAll(Arrays.asList(reservation1, reservation2));
	}

}
