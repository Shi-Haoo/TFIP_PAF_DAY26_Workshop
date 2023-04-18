package practice.workshop26.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import practice.workshop26.model.Game;

@Repository
public class BoardGameRepository {
    
    @Autowired
    MongoTemplate template;

    public List<Game> getGamesByLimitAndOffset(int limit, int offset){
        
        Query q = new Query().skip(offset).limit(limit);

        return template.find(q, Document.class, "games").stream()
                                                         .map(d -> Game.convertFromDocument(d))
                                                         .toList();
                                                    
    }

}
