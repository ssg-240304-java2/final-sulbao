package com.finalproject.sulbao.board.common;

import com.finalproject.sulbao.board.domain.TopTag;
import com.finalproject.sulbao.board.repository.PostRepository;
import com.finalproject.sulbao.board.repository.TopTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TopTagScheduler {

    private final PostRepository postRepository;
    private final TopTagRepository topTagRepository;

    @Scheduled(cron = "0 0 6 * * ?")
    public void updateTopTags() {
        List<Object[]> popularTags = postRepository.findTopTagsWithCount();
        topTagRepository.deleteAll();

        popularTags.stream()
                .limit(15)
                .map(result -> new TopTag((String) result[0], (Long) result[1]))
                .forEach(topTagRepository::save);
    }

    public List<String> getTopTagNames() {
        return topTagRepository.findAll().stream().map(TopTag::getName).toList();
    }

}
