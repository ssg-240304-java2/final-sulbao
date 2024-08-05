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
import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback(false)
class BoardTest {

    @Autowired BoardCategoryRepository boardCategoryRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired PostImageRepository postImageRepository;
    @Autowired PostRepository postRepository;

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
        BoardCategory zzanQuestion = boardCategoryRepository.findById(2L).orElseThrow();
        BoardCategory zzanPost = boardCategoryRepository.findById(3L).orElseThrow();

        // Create PostImage
        /*PostImage fooImage = PostImage.createPostImage("foo");
        PostImage barImage = PostImage.createPostImage("bar");*/

        // Find PostImage
        PostImage fooImage = postImageRepository.findById(1L).orElseThrow();
        PostImage barImage = postImageRepository.findById(2L).orElseThrow();
        List<PostImage> postImages = List.of(fooImage, barImage);

        // Create Post
        /*Post post = Post.createPost(john, zzanFeed, "Hello, World!", "Have a nice day!", postImages);
        postRepository.save(post);*/

    }

}
