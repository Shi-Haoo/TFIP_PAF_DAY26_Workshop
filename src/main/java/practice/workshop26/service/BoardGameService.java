package practice.workshop26.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import practice.workshop26.model.Game;
import practice.workshop26.repository.BoardGameRepository;

@Service
public class BoardGameService {
    
    @Autowired
    BoardGameRepository repo;

    public List<Game> getGamesByLimitAndOffset(int limit, int offset){

        return repo.getGamesByLimitAndOffset(limit, offset);


    }

    public List<Game> getGamesByRank(int limit, int offset, Direction dir, String fieldToSort){

        return repo.getGamesByRank(limit, offset, dir, fieldToSort);


    }

    public Game getGamesByObjectId(String objectId){

        if(repo.getGamesByObjectId(objectId).get().isEmpty()){
            return null;
        }

        return Game.convertFromDocument(repo.getGamesByObjectId(objectId).get());
    }

    
    public Optional<Game> getGamesById(String id){
        return repo.getGamesById(id);
    }

}
