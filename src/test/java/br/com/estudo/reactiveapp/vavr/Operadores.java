package br.com.estudo.reactiveapp.vavr;

import br.com.estudo.reactiveapp.domain.model.Person;
import br.com.estudo.reactiveapp.domain.model.User;
import br.com.estudo.reactiveapp.domain.util.PersonValidator;
import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Operadores {

    //@Test
    public void conhecendoOption(){
        Option<User> noneOption = Option.of(null);
        Option<User> someOption = Option.of(new User(1L,"Teste"));

        assertEquals("Teste", someOption.get().getName());
        assertEquals("None", noneOption.toString());
    }

    //@Test
    public void verificandoValorNulo(){
        String nome = "Fabricio";
        Option<String> nomeOption = Option.of(nome);
        assertEquals("Fabricio", nomeOption.getOrElse("Not Fabricio"));

        nome = null;
        assertEquals("Not Fabricio", nomeOption.getOrElse("Not Fabricio"));
    }

    //@Test
    public void criandoTuples2(){
        Tuple2<String, Integer> java8 = Tuple.of("Java", 8);
        String element1 = java8._1;
        int element2 = java8._2;

        assertEquals("Java", element1);
        assertEquals(8, element2);
    }

    //@Test
    public void criandoTuple3(){
        Tuple3<String, Integer, Double> java8 = Tuple.of("Java", 8, 1.8);
        String element1 = java8._1;
        int element2 = java8._2;
        double element3 = java8._3;

        assertEquals("Java", element1);
        assertEquals(8, element2);
        assertEquals(1.8, element3, 0.1);
    }

    //@Test
    public void utilizandoBlocoDeExcecao(){
        Try<Integer> result = Try.of(() -> 1/0);
        System.out.println(result.getCause());
        assertTrue(result.isFailure());
    }

    //@Test
    public void colocarValorPadraoEmCasaDeExcessao(){
        Try<Integer> computation = Try.of(() -> 1/0);
        int valorCasoDeErro = computation.getOrElse(-1);
        assertEquals(-1, valorCasoDeErro);
    }

    //@Test
    public void esperandoExcessao(){
        Try<Integer> result = Try.of(() -> 1 / 0);
        assertThrows(ArithmeticException.class, () ->
                result.getOrElseThrow(() -> new ArithmeticException()));
    }

    //@Test
    public void listaComVavr(){
        List<Integer> lista = List.of(1,2,3);
        assertEquals(3, lista.size());
        assertEquals(new Integer(1), lista.get(0));
    }

    //@Test
    public void somarDadosNaLista(){
        int sum = List.of(1,2,3).sum().intValue();
        assertEquals(6, sum);
    }

    //@Test
    public void funcaoLazy(){
        Lazy<Double> lazy = Lazy.of(Math::random);
        assertFalse(lazy.isEvaluated()); // ainda não foi executada a função

        double vl1 = lazy.get();
        System.out.println(vl1);
        assertTrue(lazy.isEvaluated());

        double vl2 = lazy.get();
        System.out.println(vl2);
        assertEquals(vl1, vl2, 0.1);
    }

    //@Test
    public void sequenciaDeValidacao(){
        PersonValidator personValidator = new PersonValidator();

        Validation<Seq<String>, Person> valid =
                personValidator.validatePerson("John Doe", 30);

        Validation<Seq<String>, Person> invalid =
                personValidator.validatePerson("John? Doe!4", -1);
        assertEquals(
                "Valid(Person(nome=John Doe, age=30))",
                valid.toString());

        assertEquals(
                "Invalid(List(Invalid characters in name: ?!4, Age must be at least 0))",
        invalid.toString());
    }
}
