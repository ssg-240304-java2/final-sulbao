package com.finalproject.sulbao.board;

import com.finalproject.sulbao.board.domain.BoardCategory;
import com.finalproject.sulbao.board.domain.Member;
import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.board.domain.PostImage;
import com.finalproject.sulbao.board.repository.BoardCategoryRepository;
import com.finalproject.sulbao.board.repository.MemberRepository;
import com.finalproject.sulbao.board.repository.PostImageRepository;
import com.finalproject.sulbao.board.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
class BoardTest {

    @Autowired
    BoardCategoryRepository boardCategoryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostImageRepository postImageRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    void load() {
        // Create Member
        /*Member john = new Member("john", "john", "john", "john@email.com", "user");
        Member jane = new Member("jane", "jane", "jane", "jane@email.com", "expert");
        memberRepository.save(john);
        memberRepository.save(jane);*/

        // Find Member
        Member john = memberRepository.findById(1L).orElseThrow();
        Member jane = memberRepository.findById(2L).orElseThrow();


        // Create Board
        /*BoardCategory zzanFeed = new BoardCategory("짠피드");
        BoardCategory zzanQuestion = new BoardCategory("술술무");
        BoardCategory zzanPost = new BoardCategory("술포스트");
        boardCategoryRepository.save(zzanFeed);
        boardCategoryRepository.save(zzanQuestion);
        boardCategoryRepository.save(zzanPost);*/

        // Find Board
        BoardCategory zzanFeed = boardCategoryRepository.findById(1L).orElseThrow();
        BoardCategory zzanPost = boardCategoryRepository.findById(3L).orElseThrow();

        postRepository.deleteAll();

        // Create PostImage
        /*PostImage fooImage = PostImage.createPostImage("foo");
        PostImage barImage = PostImage.createPostImage("bar");*/

        // Create Post
        postRepository.deleteAll();
        for (int i = 0; i < 2000; i++) {
            postRepository.save(Post.createPost(john, zzanFeed, "Hello, World!", "Have a nice day!", List.of(PostImage.createPostImage("https://img6.yna.co.kr/etc/inner/KR/2019/11/05/AKR20191105064200060_01_i_P4.jpg"))));
        }

    }

}
