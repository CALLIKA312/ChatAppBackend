package com.example.chatapptrpo.repositories;

import com.example.chatapptrpo.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    //@Query("select c from Chat inner join c.user_chats uc inner join uc.user u where u.id in :userId")
    //Set<Chat> findAllByUsersIn(@Param("userId") Long userId);

    Chat getChatById(Long id);

    Set<Chat> findByUsersId(Long userId);
}

//@Query("select r from Recipe r inner join r.recipeIngrs ri inner join ri.ingredient i where i.ingrName in :names")
//public Set<Recipe> findAllByIngrNameIn(@Param("names") Collection<String> ingredientNames)
