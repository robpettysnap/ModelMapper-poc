package org.example;

import org.example.dto.SimpleDto;
import org.example.dto.NestedDto;
import org.example.model.SimpleModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.List;
import java.util.stream.Collectors;

public class ModelExtensions {
    public static final ModelMapper mapper;

    static {
        // ModelMapper is convention based which can cause mis-mapping if you are not familiar
        // with the conventions. so I prefer using strict mode
        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        mapper.createTypeMap(SimpleModel.class, SimpleDto.class)
                .addMapping(SimpleModel::getNotOnDTO, SimpleDto::setNotOnModel);

        mapper.createTypeMap(SimpleModel.class, NestedDto.class)
                .addMappings(m -> {
                    m.map(SimpleModel::getInteger, (dest, v) -> dest.setInteger(v != null ? (int) v : 0));
                    m.map(SimpleModel::isBool, (dest, v) -> dest.getInside().setBool(v != null && (boolean) v));
                    m.map(SimpleModel::getString, (dest, v) -> dest.getInside().setString((String) v));
                    m.map(SimpleModel::getNotOnDTO, (dest, v) -> dest.getInside().setNotOnModel((String) v));
                });

    }

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> mapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    public static SimpleDto toDtoUnchanged(SimpleModel model) {
        var maps = new ModelMapper();
        maps.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return maps.typeMap(SimpleModel.class, SimpleDto.class).map(model);
    }

    public static SimpleDto toDto(SimpleModel model) {
        return mapper.map(model, SimpleDto.class);
    }

    public static NestedDto toNestedDto(SimpleModel model) {
        return mapper.map(model, NestedDto.class);
    }
}


