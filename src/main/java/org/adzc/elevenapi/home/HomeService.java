package org.adzc.elevenapi.home;

import org.adzc.elevenapi.common.PageResult;
import org.adzc.elevenapi.home.dto.UserCard;
import org.adzc.elevenapi.mapper.UserMapper;
import org.adzc.elevenapi.mapper.UserProfileMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {

    private final UserMapper userMapper;

    private final UserProfileMapper userProfileMapper;

    public HomeService(UserMapper userMapper, UserProfileMapper userProfileMapper) {
        this.userMapper = userMapper;
        this.userProfileMapper = userProfileMapper;
    }

    public List<UserCard> list(int page, int size, String province, String city, long userId) {
        int p = Math.max(page, 1);
        int s = Math.max(Math.min(size, 100), 1);
        int offset = (p - 1) * s;
        if ((province == null || province.isBlank()) && (city == null || city.isBlank())) {
            return userMapper.selectUserCards(s, offset,userId);
        }
        return userMapper.selectUserCardsFiltered(s, offset, province, city,userId);
    }

    public PageResult<UserCard> page(int page, int size, String province, String city,long userId) {
        int p = Math.max(page, 1);
        int s = Math.max(Math.min(size, 100), 1);
        int offset = (p - 1) * s;

        List<UserCard> list;
        long total;
        if ((province == null || province.isBlank()) && (city == null || city.isBlank())) {
            list = userMapper.selectUserCards(s, offset,userId);
            total = userMapper.countAllForFeed();
        } else {
            list = userMapper.selectUserCardsFiltered(s, offset, province, city,userId);
            total = userMapper.countForFeedFiltered(province, city);
        }
        return new PageResult<>(list, p, s, total);
    }
}
