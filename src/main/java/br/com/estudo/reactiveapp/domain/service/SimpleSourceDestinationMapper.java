package br.com.estudo.reactiveapp.domain.service;

import br.com.estudo.reactiveapp.domain.model.SimpleDestination;
import br.com.estudo.reactiveapp.domain.model.SimpleSource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SimpleSourceDestinationMapper {
    SimpleDestination sourceDestination(SimpleSource source);
    SimpleSource destinationToSource(SimpleDestination destination);
}
