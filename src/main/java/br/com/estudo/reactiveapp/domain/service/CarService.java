package br.com.estudo.reactiveapp.domain.service;

import br.com.estudo.reactiveapp.domain.model.Car;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarService {

    private List<Car> carros =  new ArrayList<>();

    public CarService(){
        var car1 = new Car(UUID.randomUUID().toString(), "Fusca");
        var car2 = new Car(UUID.randomUUID().toString(), "GOL");
        carros.add(car1);
        carros.add(car2);
    }

    public Car get(String id){
        var obj = carros.stream().filter(p -> p.getId().equals(id)).collect(Collectors.toList());

        if(obj.isEmpty()){
            return new Car();
        }

        return obj.get(0);
    }

    public Mono<Car> save(Car car){
        var entity = new Car(UUID.randomUUID().toString(), car.getName());
        carros.add(entity);
        return Mono.just(entity);
    }

    public List<Car> findAll(){
        return carros;
    }
}
