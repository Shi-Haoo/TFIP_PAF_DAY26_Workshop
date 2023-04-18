package practice.workshop26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
}
