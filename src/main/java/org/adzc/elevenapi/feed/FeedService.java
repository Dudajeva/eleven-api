package org.adzc.elevenapi.feed;


import lombok.RequiredArgsConstructor;
import org.adzc.elevenapi.domain.FeedLike;
import org.adzc.elevenapi.domain.FeedPost;
import org.adzc.elevenapi.feed.dto.FeedItemDTO;
import org.adzc.elevenapi.mapper.FeedLikeMapper;
import org.adzc.elevenapi.mapper.FeedPostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedPostMapper postMapper;
    private final FeedLikeMapper likeMapper;

    public List<FeedItemDTO> list(Long viewerId, int offset, int size, String province, String city) {
        return postMapper.list(viewerId, offset, size, province, city);
    }

    @Transactional
    public void create(Long userId, String text, String photoUrl) {
        FeedPost feedPost = new FeedPost();
        feedPost.setUserId(userId);
        feedPost.setText(text);
        feedPost.setPhotoUrl(photoUrl);
        postMapper.insert(feedPost);
    }

    @Transactional
    public void like(Long userId, Long postId) {
        FeedLike feedLike = new FeedLike();
        feedLike.setUserId(userId);
        feedLike.setPostId(postId);
        likeMapper.insert(feedLike);
    }

    @Transactional
    public void unlike(Long userId, Long postId) {
        likeMapper.deleteLike(userId, postId);
    }
}
