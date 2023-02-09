package com.imooc.reader.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.reader.entity.Book;
import com.imooc.reader.entity.Evaluation;
import com.imooc.reader.entity.Member;
import com.imooc.reader.mapper.EvaluationMapper;
import com.imooc.reader.mapper.MemberMapper;
import com.imooc.reader.service.BookService;
import com.imooc.reader.service.EvaluationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("evaluationService")
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class EvaluationServiceImpl implements EvaluationService {
    @Resource
    private EvaluationMapper evaluationMapper;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private BookService bookService;
    /**
     * 按照書本id查詢有效的評論
     *
     * @param bookId 書本id
     * @return 評論List
     */
    @Override
    public List<Evaluation> selectByBookId(Long bookId) {
        //依照評論的bookId查詢Book物件並注入到Evaluation中
        Book book=bookService.selectById(bookId);
        QueryWrapper<Evaluation> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("book_id",bookId);
        //有效的評論
        queryWrapper.eq("state","enable");
        queryWrapper.orderByDesc("create_time");
        List<Evaluation> evaluationList=evaluationMapper.selectList(queryWrapper);
        //注入Evaluation的Member與Book物件
        for(Evaluation evaluation:evaluationList){
            //依照評論的memberId查詢Member物件並注入到Evaluation中
            Long memberId=evaluation.getMemberId();
            Member member=memberMapper.selectById(memberId);
            evaluation.setMember(member);
            //注入Book物件
            evaluation.setBook(book);
        }
        return evaluationList;
    }
}
