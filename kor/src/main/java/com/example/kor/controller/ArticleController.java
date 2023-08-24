package com.example.kor.controller;

import com.example.kor.dto.ArticleDto;
import com.example.kor.mapper.ConfigMapper;
import com.example.kor.service.ArticleSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ArticleSrv articleSrv;

    @GetMapping("/article/list")
    public String getList(@RequestParam String code, Model model,
                          @RequestParam(value = "searchType", defaultValue = "") String searchType,
                          @RequestParam(value = "words", defaultValue = "") String words,
                          @RequestParam(value = "page", defaultValue = "1") int page) {

        articleSrv.pageCalc(page, code);

        model.addAttribute("config", configMapper.getConfigOne(code));
        model.addAttribute("art", articleSrv.getList(code, searchType, words, page));
        model.addAttribute("pagination", articleSrv.pageCalc(page, code));
        model.addAttribute("totalCount", configMapper.getArticleCount(code));

        return "article/list";
    }

    @GetMapping("/article/write")
    public String getWrite(@RequestParam String code, Model model) {
        model.addAttribute("config", configMapper.getConfigOne(code));
        return "article/write";
    }

    @PostMapping("/article/write")
    public String setWrite(@ModelAttribute ArticleDto articleDto) {

        int cnt = configMapper.getMaxCnt(articleDto.getCode());
        articleDto.setGrp(cnt);

        configMapper.setWrite(articleDto); // grp,depth 값이 나와야 함
        return "redirect:/article/list?code=" + articleDto.getCode();
    }

    @GetMapping("/article/delete")
    public String delArticle(@ModelAttribute ArticleDto articleDto) {
        configMapper.delArticle(articleDto);
        return "redirect:/article/list?code=" + articleDto.getCode();
    }

    @GetMapping("/article/edit")
    public String getEdit(@ModelAttribute ArticleDto articleDto, Model model) {
        model.addAttribute("config", configMapper.getConfigOne(articleDto.getCode()));
        model.addAttribute("art", configMapper.getEdit(articleDto));
        return "/article/edit";
    }

    @PostMapping("/article/edit")
    public String setEdit(@ModelAttribute ArticleDto articleDto) {
        configMapper.setEdit(articleDto);
        return "redirect:/article/list?code=" + articleDto.getCode();
    }

    @GetMapping("/article/view")
    public String getView(Model model, @ModelAttribute ArticleDto articleDto) {
        model.addAttribute("config", configMapper.getConfigOne(articleDto.getCode()));
        model.addAttribute("art", configMapper.getEdit(articleDto));
        return "article/view";
    }

    @GetMapping("/article/reply")
    public String getReply(Model model, @ModelAttribute ArticleDto articleDto) {
        model.addAttribute("config", configMapper.getConfigOne(articleDto.getCode()));
        model.addAttribute("art", configMapper.getEdit(articleDto));
        return "article/reply";
    }

    @PostMapping("/article/reply")
    public String setReply(@ModelAttribute ArticleDto articleDto) {

        articleDto.setDepth(articleDto.getDepth() + 1);

        configMapper.setReply(articleDto);

        return "redirect:/article/list?code=" + articleDto.getCode();
    }
}
