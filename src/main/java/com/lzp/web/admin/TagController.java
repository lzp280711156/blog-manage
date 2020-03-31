package com.lzp.web.admin;

import com.lzp.po.Tag;
import com.lzp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    private static final String TAGS = "/admin/tags";
    private static final String TAGS_INPUT = "/admin/tags-input";
    private static final String REDIRECT_TAGS = "redirect:/admin/tags";

    @Autowired
    private TagService tagService;

    /**
     * 分页查询标签列表
     *
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 5, page = 0, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<Tag> tags = this.tagService.listTag(pageable);
        model.addAttribute("tags", tags);
        return TAGS;
    }

    /**
     * 跳转新增标签页面
     *
     * @param model
     * @return
     */
    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return TAGS_INPUT;
    }

    /**
     * 新增标签
     *
     * @param tag
     * @param bindingResult
     * @param attributes
     * @return
     */
    @PostMapping("/tags")
    public String input(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes attributes) {
        // 验证标签是否已存在
        Tag tag1 = this.tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            bindingResult.rejectValue("name", "nameError", "该标签已存在，不能重复新增");
        }
        // 校验标签名称是否为null
        if (bindingResult.hasErrors()) {
            return TAGS_INPUT;
        }
        // 新增标签
        Tag resultTag = this.tagService.saveTag(tag);
        if (resultTag == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return REDIRECT_TAGS;
    }

    /**
     * 删除标签
     *
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable("id") Long id, RedirectAttributes attributes) {
        this.tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_TAGS;
    }

    /**
     * 跳转标签修改页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}/input")
    public String toEditTag(@PathVariable("id") Long id, Model model) {
        Tag tag = this.tagService.getTagById(id);
        model.addAttribute("tag", tag);
        return TAGS_INPUT;
    }

    /**
     * 修改标签
     *
     * @param id
     * @param tag
     * @param bindingResult
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String editTag(@PathVariable("id") Long id, @Valid Tag tag, BindingResult bindingResult, RedirectAttributes attributes) {
        // 校验标签名称是否已存在
        Tag tag1 = this.tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            bindingResult.rejectValue("name", "nameError", "该标签已存在，不能重复新增");
        }
        // 检验标签名称是否为null
        if (bindingResult.hasErrors()) {
            return TAGS_INPUT;
        }
        // 修改标签
        Tag resultTag = this.tagService.updateTag(id, tag);
        if (resultTag == null) {
            attributes.addFlashAttribute("message", "修改失败");
        } else {
            attributes.addFlashAttribute("message", "修改成功");
        }
        return REDIRECT_TAGS;
    }

}
