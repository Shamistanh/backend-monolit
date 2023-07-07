package com.pullm.backendmonolit.mapper;

import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.models.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User mapToUser(RegisterRequest studentRequest);

}
