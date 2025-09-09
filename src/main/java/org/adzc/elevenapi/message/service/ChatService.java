package org.adzc.elevenapi.message.service;

import org.adzc.elevenapi.domain.ChatConversation;
import org.adzc.elevenapi.domain.ChatMessage;
import org.adzc.elevenapi.mapper.ChatConversationListMapper;
import org.adzc.elevenapi.mapper.ChatConversationMapper;
import org.adzc.elevenapi.mapper.ChatMemberMapper;
import org.adzc.elevenapi.mapper.ChatMessageMapper;
import org.adzc.elevenapi.message.dto.ConversationItemDTO;
import org.adzc.elevenapi.message.dto.MessageDTO;
import org.adzc.elevenapi.util.TextPreviewUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {

    private final ChatConversationMapper convExt;
    private final ChatConversationListMapper listMapper;
    private final ChatMemberMapper memExt;
    private final ChatMessageMapper msgExt;

    public ChatService(ChatConversationMapper convExt,
                       ChatConversationListMapper listMapper,
                       ChatMemberMapper memExt,
                       ChatMessageMapper msgExt) {
        this.convExt = convExt;
        this.listMapper = listMapper;
        this.memExt = memExt;
        this.msgExt = msgExt;
    }

    /** 校验成员并返回对端ID */
    public Long ensureMember(Long me, Long conversationId) {
        ChatConversation c = convExt.findById(conversationId);
        if (c == null) throw new IllegalArgumentException("会话不存在");
        Boolean joined = memExt.exists(conversationId, me);
        if (joined == null || !joined) throw new IllegalStateException("非会话成员");
        return me.equals(c.getU1Id()) ? c.getU2Id() : c.getU1Id();
    }

    /** 获取对端ID */
    public Long getPeerId(Long me, Long conversationId) {
        ChatConversation c = convExt.findById(conversationId);
        if (c == null) throw new IllegalArgumentException("会话不存在");
        return me.equals(c.getU1Id()) ? c.getU2Id() : c.getU1Id();
    }

    /** 会话列表（消息页） */
    public List<ConversationItemDTO> listConversations(Long me, int page, int size) {
        int limit = Math.min(Math.max(size, 1), 50);
        int offset = (Math.max(page, 1) - 1) * limit;
        return listMapper.list(me, limit, offset);
    }

    /** 历史消息分页（倒序） */
    public List<MessageDTO> listMessages(Long me, Long cid, Long cursor, int size) {
        ensureMember(me, cid);
        var list = msgExt.pageDesc(cid, cursor, Math.min(size, 50));
        return list.stream().map(m -> new MessageDTO(
                m.getId(), m.getConversationId(), m.getSenderId(),
                m.getType(), m.getContent(), m.getImageUrl(), m.getCreatedAt()
        )).toList();
    }

    /** 保存消息并更新会话冗余，返回(mid, timeMillis, preview) */
    @Transactional
    public Saved saveMessage(Long senderId, Long conversationId, String msg, String imgUrl) {
        ChatMessage m = new ChatMessage();
        m.setConversationId(conversationId);
        m.setSenderId(senderId);
        if (imgUrl != null && !imgUrl.isBlank()) {
            m.setType(2);
            m.setImageUrl(imgUrl);
        } else {
            m.setType(1);
            m.setContent(msg);
        }
        msgExt.insert(m); // 自增

        String preview = TextPreviewUtil.from(m);
        convExt.updateLast(conversationId, m.getId(), preview);

        return new Saved(m.getId(), Instant.now().toEpochMilli(), preview);
    }

    /** 已读 */
    @Transactional
    public Long markRead(Long me, Long conversationId, Long lastReadMessageId) {
        memExt.updateLastRead(conversationId, me, lastReadMessageId);
        return getPeerId(me, conversationId);
    }

    /** 未读统计 */
    public int calcUnread(Long uid, Long conversationId) {
        Long lastRead = memExt.getLastRead(conversationId, uid);
        long cursor = lastRead == null ? 0L : lastRead;
        return msgExt.countUnread(conversationId, uid, cursor);
    }

    /** open 会话（从个人页发起）——若不存在则新建 */
    @Transactional
    public Long openOrCreate(Long me, Long targetId) {
        long u1 = Math.min(me, targetId), u2 = Math.max(me, targetId);
        Long cid = convExt.findIdByUsers(u1, u2);
        if (cid != null) return cid;

        // 用你的基础 Mapper 插入（此处只写逻辑，SQL 在 Ext XML 给出）
        convExt.insertConversation(u1, u2);           // 返回自增ID：在 XML 里用 useGeneratedKeys
        cid = convExt.findIdByUsers(u1, u2);

        memExt.insertMember(cid, u1);
        memExt.insertMember(cid, u2);
        return cid;
    }

    /** 保存后的返回体 */
    public record Saved(long mid, long time, String preview) {}
}
