package com.finalproject.sulbao;

import com.finalproject.sulbao.board.domain.BoardCategory;
import com.finalproject.sulbao.board.domain.Comment;
import com.finalproject.sulbao.board.domain.Post;
import com.finalproject.sulbao.board.domain.PostImage;
import com.finalproject.sulbao.board.repository.*;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.MemberInfo;
import com.finalproject.sulbao.login.model.entity.RoleType;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.repository.MemberInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@Rollback(false)
class EntityTest {

    @Autowired
    BoardCategoryRepository boardCategoryRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    PostImageRepository postImageRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberInfoRepository memberInfoRepository;
    @Autowired
    private LikeRepository likeRepository;

    @Test
    void load() {
       /* insertUser();
        insertBoardCategory();

        Login john = loginRepository.findById(1L).orElseThrow();
        Login jane = loginRepository.findById(2L).orElseThrow();

        BoardCategory zzanFeed = boardCategoryRepository.findById(1L).orElseThrow();
        BoardCategory zzanPost = boardCategoryRepository.findById(2L).orElseThrow();

        insertPost(john, zzanPost);
        insertPost(jane, zzanPost);

        Post post = postRepository.findById(1L).orElseThrow();

        insertComment(john, post);
        insertComment(jane, post);*/

        Post post = postRepository.findById(210L).orElseThrow();
        post.getTags().add("test1");
        post.getTags().add("test2");
    }

    private void insertComment(Login john, Post post) {
        Comment comment = Comment.createComment("The greatest glory in living lies not in never falling, but in rising every time we fall.", john, post);
        commentRepository.save(comment);
    }

    private void insertPost(Login john, BoardCategory zzanPost) {
        for (int i = 0; i < 100; i++) {
            PostImage postImage = PostImage.createPostImage("post_image");
            String content = """
                    <section>
                                <h2 style="color: #6c757d; border-bottom: 2px solid #6c757d; padding-bottom: 10px;">전통주의 종류와 특징</h2>
                                <p>
                                    전통주는 크게 막걸리, 약주, 소주로 나눌 수 있습니다.\s
                                </p>
                                <h3 style="color: #6c757d;">막걸리</h3>
                                <p>
                                    막걸리는 발효된 곡물을 거르지 않고 만든 탁주로, 알코올 도수가 낮아 누구나 부담 없이 즐길 수 있습니다. 특유의 청량감과 고소한 맛이 특징이며, 최근에는 다양한 맛과 향을 더한 신제품들도 많이 출시되고 있습니다.
                                </p>
                                <h3 style="color: #6c757d;">약주</h3>
                                <p>
                                    약주는 정제된 곡물을 발효시켜 만든 청주로, 알코올 도수가 막걸리보다 높습니다. 맑고 깨끗한 맛이 특징이며, 전통적으로 제사나 의례에 사용되는 술입니다. 다양한 한약재를 첨가하여 건강을 돕는 효과를 기대할 수 있는 약주도 있습니다.
                                </p>
                                <h3 style="color: #6c757d;">소주</h3>
                                <p>
                                    소주는 쌀, 보리 등을 증류하여 만든 술로, 알코올 도수가 높아 소량으로도 강한 취기를 느낄 수 있습니다. 전통 소주는 현대의 희석식 소주와는 달리 깊고 풍부한 맛이 특징이며, 지역마다 독특한 소주가 전해져 내려옵니다.
                                </p>
                            </section>
                                        
                            <section>
                                <h2 style="color: #6c757d; border-bottom: 2px solid #6c757d; padding-bottom: 10px;">전통주의 현대적 재해석</h2>
                                <p>
                                    현대에 들어 전통주는 새로운 모습으로 재탄생하고 있습니다. 젊은 세대의 입맛과 취향을 반영하여 다양한 맛과 향을 더한 전통주가 출시되고 있으며, 전통주의 가치를 알리기 위한 다양한 노력들이 이어지고 있습니다. 특히, 전통주의 제조 과정을 체험할 수 있는 프로그램이나, 전통주를 활용한 칵테일 등 새로운 시도가 주목받고 있습니다.
                                </p>
                                <p>
                                    또한, 전통주는 한국의 문화유산으로서 세계 시장에서도 그 가치를 인정받고 있습니다. 국제 주류 시장에서의 수상 경력과 해외 수출 증가가 이를 뒷받침하고 있으며, 전통주의 독특한 맛과 향은 세계인들에게 큰 호응을 얻고 있습니다.
                                </p>
                            </section>
                                        
                            <section>
                                <h2 style="color: #6c757d; border-bottom: 2px solid #6c757d; padding-bottom: 10px;">전통주의 미래</h2>
                                <p>
                                    전통주는 단순한 술이 아니라, 한국의 역사와 문화를 담은 중요한 유산입니다. 앞으로도 전통주의 가치를 지키고 발전시키기 위한 노력이 계속되어야 할 것입니다. 전통 제조 방식의 보존과 함께, 현대적 감각을 더한 새로운 시도를 통해 전통주는 더욱 풍부하고 다채로운 모습으로 발전해 나갈 것입니다.
                                </p>
                                <p>
                                    전통주 전문가로서, 이러한 변화와 발전을 주의 깊게 살펴보고, 전통주의 매력을 널리 알리는 역할을 하고자 합니다. 여러분도 전통주에 관심을 가지고 다양한 전통주를 경험해보시길 권합니다. 전통주의 깊은 맛과 향 속에서 한국의 역사와 문화를 느껴보세요.
                                </p>
                                <img src="https://innertrip.co.kr/wp-content/uploads/2022/08/%EC%A0%84%ED%86%B5%EC%A3%BC-%ED%85%8C%EC%9D%B4%EC%8A%A4%ED%8C%85-1.png" style="margin-left: 15%">
                            </section>
                    """;

            Post post = Post.createPost(john, zzanPost, "Hello, World!", content, postImage);
            postRepository.save(post);
        }
    }

    private void insertBoardCategory() {
        BoardCategory zzanFeed = new BoardCategory("짠 피드");
        BoardCategory zzanPost = new BoardCategory("술 포스트");
        boardCategoryRepository.save(zzanFeed);
        boardCategoryRepository.save(zzanPost);
    }

    private void insertUser() {
        MemberInfo memberInfo;
        Login login;

        login = new Login();
        login.setUserId("John");
        login.setUserPw("password");
        login.setUserRole(RoleType.MEMBER);
        login.setName("John");
        login.setBirth("2000-01-01");
        login.setPhone("010-0000-0000");
        login.setEmail("john@gmail.com");
        loginRepository.save(login);

        memberInfo = new MemberInfo();
        memberInfo.setVerifyageDate(LocalDateTime.now());
        memberInfo.setUser(login);
        login.setMemberInfo(memberInfo);
        memberInfo.setProfileImg("profile_image");
        memberInfo.setProfileName("John");
        memberInfo.setProfileText("Hello, World!");
        memberInfoRepository.save(memberInfo);

        login = new Login();
        login.setUserId("Jane");
        login.setUserPw("password");
        login.setUserRole(RoleType.MEMBER);
        login.setName("Jane");
        login.setBirth("2000-01-01");
        login.setPhone("010-0000-0000");
        login.setEmail("jane@gmail.com");
        loginRepository.save(login);

        memberInfo = new MemberInfo();
        memberInfo.setVerifyageDate(LocalDateTime.now());
        memberInfo.setUser(login);
        login.setMemberInfo(memberInfo);
        memberInfo.setProfileImg("profile_image");
        memberInfo.setProfileName("Jane");
        memberInfo.setProfileText("Hello, World!");
        memberInfoRepository.save(memberInfo);
    }

}
