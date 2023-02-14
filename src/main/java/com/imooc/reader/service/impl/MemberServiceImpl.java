package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.reader.entity.Evaluation;
import com.imooc.reader.entity.Member;
import com.imooc.reader.entity.MemberReadState;
import com.imooc.reader.mapper.EvaluationMapper;
import com.imooc.reader.mapper.MemberMapper;
import com.imooc.reader.mapper.MemberReadStateMapper;
import com.imooc.reader.service.MemberService;
import com.imooc.reader.service.exception.BussinessException;
import com.imooc.reader.service.utils.MD5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("memberService")
//寫操作比較多:開啟事務管理
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class MemberServiceImpl implements MemberService {
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberReadStateMapper memberReadStateMapper;
    @Resource
    private EvaluationMapper evaluationMapper;
    /**
     * 會員註冊:建立一個會員，並寫入資料庫
     *
     * @param username 用戶名稱
     * @param password 用戶密碼
     * @param nickname 用戶暱稱
     * @return 會員物件
     */
    @Override
    public Member createMember(String username, String password, String nickname) {
        //檢查用戶名是否存在
        QueryWrapper<Member> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<Member> memberList=memberMapper.selectList(queryWrapper);
        //判斷用戶名稱是否被使用過
        if(memberList.size()>0){
            //使用過:拋出異常
            throw new BussinessException("M01","用戶名稱已存在");
        }
        //允許使用用戶名稱
        //開始創建會員
        Member member=new Member();
        //設定用戶名稱與暱稱
        member.setUsername(username);
        member.setNickname(nickname);
        //對密碼進行混淆(鹽值設定為4位整數)
        Integer salt=new Random().nextInt(1000)+1000;
        //執行MD5加密
        String md5=MD5Utils.md5Digest(password,salt);
        //設定鹽值與用戶密碼
        member.setSalt(salt);
        member.setPassword(md5);
        //設定會員創建時間
        member.setCreateTime(new Date());
        //將會員寫入資料庫
        memberMapper.insert(member);
        return member;
    }

    /**
     * 用戶登入:檢查用戶名稱與密碼
     *
     * @param username 用戶名稱
     * @param password 用戶密碼
     * @return 會員物件
     */
    @Override
    public Member checkLogin(String username, String password) {
        //先檢查用戶名稱是否存在
        QueryWrapper<Member> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        Member member=memberMapper.selectOne(queryWrapper);
        if(member==null){ //用戶名稱不存在
            throw new BussinessException("M02","用戶不存在");
        }
        //比對用戶的密碼
        String md5=MD5Utils.md5Digest(password,member.getSalt());
        if(!md5.equals(member.getPassword())){ //密碼不正確
            throw new BussinessException("M03","用戶密碼不正確");
        }
        return member;
    }

    /**
     * 依照會員id與書本id查詢會員是否對當前書本評論過
     *
     * @param memberId 會員id
     * @param bookId   書本id
     * @return 會員閱讀狀態物件
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
    public MemberReadState selectMemberReadState(Long memberId, Long bookId) {
        QueryWrapper<MemberReadState> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("book_id",bookId);
        MemberReadState memberReadState=memberReadStateMapper.selectOne(queryWrapper);
        return memberReadState;
    }

    /**
     * 更新閱讀狀態
     *
     * @param memberId  會員id
     * @param bookId    書本id
     * @param readState 閱讀狀態
     * @return 閱讀狀態物件
     */
    @Override
    public MemberReadState updateMemberReadState(Long memberId, Long bookId, Integer readState) {
        QueryWrapper<MemberReadState> queryWrapper=new QueryWrapper<>();
        //先查詢會員是否有閱讀狀態
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("book_id",bookId);
        MemberReadState memberReadState=memberReadStateMapper.selectOne(queryWrapper);

        if(memberReadState!=null){
            //更新閱讀狀態
            memberReadState.setReadState(readState);
            memberReadStateMapper.updateById(memberReadState);
        }else{
            //建立閱讀狀態並加入資料庫
            memberReadState=new MemberReadState();
            memberReadState.setMemberId(memberId);
            memberReadState.setBookId(bookId);
            memberReadState.setReadState(readState);
            memberReadState.setCreateTime(new Date());
            memberReadStateMapper.insert(memberReadState);
        }
        return memberReadState;
    }

    /**
     * 發布新的短評
     *
     * @param memberId 會員id
     * @param bookId   書本id
     * @param score    評論的分數
     * @param content  評論的內容
     * @return 評論物件
     */
    @Override
    public Evaluation evaluate(Long memberId, Long bookId, Integer score, String content) {
        Evaluation evaluation=new Evaluation();
        evaluation.setMemberId(memberId);
        evaluation.setBookId(bookId);
        evaluation.setScore(score);
        evaluation.setContent(content);
        evaluation.setCreateTime(new Date());
        evaluation.setState("enable");
        evaluation.setEnjoy(0); //剛新增的點讚數量
        evaluationMapper.insert(evaluation);
        return evaluation;
    }

    /**
     * 短評點讚
     *
     * @param evaluationId 短評編號
     * @return 評論物件
     */
    @Override
    public Evaluation enjoy(Long evaluationId) {
        Evaluation evaluation=evaluationMapper.selectById(evaluationId);
        //更新點讚的數量
        evaluation.setEnjoy(evaluation.getEnjoy()+1);
        evaluationMapper.updateById(evaluation);
        return evaluation;
    }
}
