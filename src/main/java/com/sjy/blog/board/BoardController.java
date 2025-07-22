package com.sjy.blog.board;

import com.sjy.blog._core.common.PageLink;
import com.sjy.blog.user.User;
import com.sjy.blog.user.UserJpaRepository;
import com.sjy.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.HTML;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    // 목록 화면
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "size", defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page -1, size, Sort.by("id").descending());
        Page<Board> boardPage = boardService.findAll(pageable);

        List<PageLink> pageLinks = new ArrayList<>();
        for(int i =0; i < boardPage.getTotalPages(); i++) {
            pageLinks.add(new PageLink(i, i+1, i == boardPage.getNumber()));
        }

        Integer previousPageNumber = boardPage.hasPrevious() ? boardPage.getNumber() : null;
        Integer nextPageNumber = boardPage.hasNext() ? boardPage.getNumber() + 2 : null;

        model.addAttribute("boardPage", boardPage);
        model.addAttribute("pageLinks", pageLinks);
        model.addAttribute("previousPageNumber", previousPageNumber);
        model.addAttribute("nextPageNumber", nextPageNumber);

        return "index";
    }

    // 상세 조회 화면
    @GetMapping("/board/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "board/detail";
    }

    // 게시글 작성 화면
    @GetMapping("/board/save-form")
    public String saveForm(HttpSession session) {

        return "board/save-form";
    }

    // 게시글 저장
    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO saveDTO, HttpSession session){
        saveDTO.validate();
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.save(saveDTO, sessionUser);
        return "redirect:/";
    }

    // 게시글 수정 화면
    @GetMapping("board/{id}/update-form")
    public String updateForm(@PathVariable(name = "id") Long boardId,
                             Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.checkBoardOwner(boardId, sessionUser.getId());
        model.addAttribute("board", boardService.findById(boardId));
        return "board/update-form";
    }

    // 게시글 수정
    @PostMapping("/board/{id}/update-form")
    public String update(@PathVariable(name = "id") Long boardId,
                         BoardRequest.UpdateDTO updateDTO, HttpSession session){
        updateDTO.validate();
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.updateById(boardId, updateDTO, sessionUser);
        return "redirect:/board/" + boardId;
    }

    // 게시글 삭제
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable(name = "id") Long id, HttpSession session) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        boardService.deleteById(id, sessionUser);
        return "redirect:/";
    }
}
