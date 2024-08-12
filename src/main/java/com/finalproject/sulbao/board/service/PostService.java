package com.finalproject.sulbao.board.service;

import com.finalproject.sulbao.board.domain.BoardCategory;
import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.board.domain.PostImage;
import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.repository.BoardCategoryRepository;
import com.finalproject.sulbao.board.repository.PostRepository;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final LoginRepository loginRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    public void updateHit(Long id) {
        postRepository.findById(id).orElseThrow().updateHit();
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public PostDto save(Long userId, String title, String content, Long boardCategoryId, String thumbnailFileName) {
        Login login = loginRepository.findById(userId).orElseThrow();
        BoardCategory boardCategory = boardCategoryRepository.findById(boardCategoryId).orElseThrow();
        PostImage postImage = PostImage.createPostImage(thumbnailFileName);
        Post post = Post.createPost(login, boardCategory, title, content, postImage);
        Post savedPost = postRepository.save(post);
        PostDto postDto = PostDto.toPostDto(savedPost);
        return postDto;
    }

    public Page<PostDto> getPostPage(int page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<Post> postPage = postRepository.findAll(pageable);
        Page<PostDto> postDtoPage = postPage.map(PostDto::toPostDto);
        return postDtoPage;
    }

    public Long count() {
        return postRepository.count();
    }

    public List<PostDto> loadMorePosts(int page) {
        Pageable pageable = PageRequest.of(page, 6);
        List<PostDto> posts = postRepository.findAll(pageable).getContent().stream().map(PostDto::toPostDto).toList();
        return posts;
    }

    public PostDto findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        PostDto postDto = PostDto.toPostDto(post);
        return postDto;
    }

    public void update(Long postId, String title, String content, String thumbnailFileName) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.update(title, content, thumbnailFileName);
    }
}
