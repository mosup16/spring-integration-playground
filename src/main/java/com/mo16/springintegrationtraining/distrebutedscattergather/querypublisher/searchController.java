package com.mo16.springintegrationtraining.distrebutedscattergather.querypublisher;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class searchController {
    private final QueryPublisherGateway queryPublisher;

    public searchController(QueryPublisherGateway queryPublisher) {
        this.queryPublisher = queryPublisher;
    }

    @GetMapping("/search")
    public List<String> search(@RequestBody String query){
        queryPublisher.publish(query);
        return List.of("result 1","result2 ,result 3");
    }
}
