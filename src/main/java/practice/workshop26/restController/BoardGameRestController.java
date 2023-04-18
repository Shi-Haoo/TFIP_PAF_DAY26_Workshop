package practice.workshop26.restController;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
