package com.sjy.blog.board;

import com.sjy.blog._core.errors.exception.Exception403;
import com.sjy.blog._core.errors.exception.Exception404;
import com.sjy.blog.user.User;
import com.sjy.blog.user.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class BoardService {

    private final BoardJpaRepository boardJpaRepository;

    // 게시글 목록
    public Page<Board> findAll(Pageable pageable) {
        log.info("게시글 목록 조회");
        Page<Board> boardPage = boardJpaRepository.findAllUser(pageable);
        return boardPage;
    }

    // 게시글 상세 조회
    public Board findById(Long id) {
        log.info("게시글 상세 조회");
        Board board = boardJpaRepository.findByIdUser(id).orElseThrow(() -> {
           return new Exception404("게시글을 찾을 수 없습니다");
        });
        return board;
    }

    // 게시글 저장
    @Transactional
    public Board save(BoardRequest.SaveDTO saveDTO, User sessionUser){
        log.info("게시글 저장 시작");
        Board board = saveDTO.toEntity(sessionUser);
        boardJpaRepository.save(board);
        log.info("저장 완료");
        return board;
    }

    // 게시글 수정
    @Transactional
    public Board updateById(Long id, BoardRequest.UpdateDTO updateDTO,
                            User sessionUser){
        log.info("게시글 수정 시작");
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> {
            return new Exception404("해당 게시글이 존재하지 않습니다");
        });

        if(!board.isOwner(sessionUser.getId())){
            throw new Exception403("본인이 작성한 게시글만 수정 가능합니다");
        }

        board.setTitle(updateDTO.getTitle());
        board.setContent(updateDTO.getContent());

        return board;
    }

    // 게시글 삭제
    @Transactional
    public void deleteById(Long id, User sessionUser){
        log.info("게시글 삭제 시작");
        Board board = boardJpaRepository.findById(id).orElseThrow(() -> {
            return new Exception404("삭제하려는 게시글이 없습니다");
        });
        if(!board.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 게시글만 삭제할 수 있습니다");
        }
    }

    public void checkBoardOwner(Long boardId, Long userId) {
        Board board = findById(boardId);
        if(!board.isOwner(userId)) {
            throw new Exception403("본인 게시글만 수정할 수 있습니다");
        }
    }
}
