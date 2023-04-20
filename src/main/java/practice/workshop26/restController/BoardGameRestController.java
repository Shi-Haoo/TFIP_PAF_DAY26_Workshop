package practice.workshop26.restController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import practice.workshop26.model.Game;
import practice.workshop26.model.Games;
import practice.workshop26.service.BoardGameService;

@RestController
public class BoardGameRestController {
    
    @Autowired
    BoardGameService svc;
    
    @GetMapping(path="/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGamesByOffsetAndLimit(@RequestParam(required = false, defaultValue = "0") int offset, @RequestParam(required = false, defaultValue = "25") int limit){
        List<Game> gamesRetrieved = svc.getGamesByLimitAndOffset(limit, offset);
        Games gamesList = new Games(limit, offset, LocalDateTime.now(), gamesRetrieved);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(gamesList.toJson().toString());
    }

    @GetMapping(path="/games/rank", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGamesByRank(@RequestParam(required = false, defaultValue = "0") int offset, @RequestParam(required = false, defaultValue = "25") int limit){
        List<Game> gamesRetrieved = svc.getGamesByRank(limit, offset, Direction.ASC, "ranking");
        Games gamesList = new Games(limit, offset, LocalDateTime.now(), gamesRetrieved);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(gamesList.toJson().toString());
    }

    //Get Game by Object id
    @GetMapping(path="/gameOid/{objectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGamesByObjectId(@PathVariable String objectId){

        Game game = null;

        //If objectId does not exist in db, it will return null. So we use try-catch 
        //to catch null pointer exception. There are other ways to handle null cases
        //i.e if(svc.getGamesByObjectId(objectId))==null).. or use Optional.empty()

        try{
            game = svc.getGamesByObjectId(objectId);
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                            .add("error message", "Game with Object Id: %s not found".formatted(objectId))
                            .build()
                            .toString());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(game.toJson()
                          .toString());
        
    }

    //Get Game by gid or Object Id
    @GetMapping(path = "game/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGamesById(@PathVariable String id){

        Optional<Game> game = svc.getGamesById(id);

        if(game.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Error Message: " + "Game with this gid or _id is not found");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(game.get().toJson().toString());

    }


}
