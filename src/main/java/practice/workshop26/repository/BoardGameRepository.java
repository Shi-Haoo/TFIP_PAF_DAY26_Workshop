package practice.workshop26.repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
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

    public List<Game> getGamesByRank(int limit, int offset, Direction dir, String fieldToSort){
        
        Query q = new Query().skip(offset).limit(limit);

        q.with(Sort.by(dir , fieldToSort));

        return template.find(q, Document.class, "games").stream()
                                                         .map(d -> Game.convertFromDocument(d))
                                                         .toList();
                                                    
    }

    public Optional<Document> getGamesByObjectId(String id){
        ObjectId objectId = new ObjectId(id);

        return Optional.ofNullable(template.findById(objectId, Document.class, "games"));
    }

    public Optional<Game> getGamesById(String id){
        
        Optional<Document> doc = null;
        
        //check whether string id is a valid 24-character hexadecimal string which represents a
        // valid ObjectId used in MongoDB. It does not mean the ObjectId exist in the database.

        if(ObjectId.isValid(id)){
            
            try{
               doc = Optional.ofNullable(template.findById(id, Document.class, "games"));
               return Optional.of(Game.convertFromDocument(doc.get()));
            }catch(NoSuchElementException e){
                return Optional.empty();
            }            
        }
        
        else{
            //If not valid object id, search by gid
            Query q = new Query();
            q.addCriteria(Criteria.where("gid").is(Integer.parseInt(id)));

            try{
            //template.find().stream().findFirst() returns a stream of results,  
            //and then returns the first element of the stream as an Optional.
                doc = (template.find(q, Document.class, "games")).stream().findFirst();
                return Optional.of(Game.convertFromDocument(doc.get()));
            }catch(NoSuchElementException e){
                return Optional.empty();
            }        
        }
    }

}
