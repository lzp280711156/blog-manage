package com.lzp.web.admin;

import com.lzp.po.Type;
import com.lzp.service.TypeService;
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
public class TypeController {

    private static final String TYPES = "admin/types";
    private static final String TYPES_INPUT = "admin/types-input";
    private static final String REDIRECT_TYPES = "redirect:/admin/types";

    @Autowired
    private TypeService typeService;

    /**
     * 分页查询
     *
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/types")
    public String types(@PageableDefault(size = 5, page = 0, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<Type> types = this.typeService.listType(pageable);
        model.addAttribute("page", types);
        return TYPES;
    }

    /**
     * 跳转新增页面
     *
     * @return
     */
    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return TYPES_INPUT;
    }

    /**
     * 跳转修改页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/input")
    public String toEditInput(@PathVariable("id") Long id, Model model) {
        Type type = this.typeService.getType(id);
        model.addAttribute("type", type);
        return TYPES_INPUT;
    }

    /**
     * 新增分类
     *
     * @param type
     * @return
     */
    @PostMapping("/types")
    public String saveType(@Valid Type type, BindingResult bindingResult, RedirectAttributes attributes) {
        // 校验该分类是否已存在
        Type type1 = this.typeService.getTypeByName(type.getName());
        if (type1 != null) {
            bindingResult.rejectValue("name", "nameError", "该分类已存在，不能重复新增");
        }
        // 校验名称是否为null
        if (bindingResult.hasErrors()) {
            return TYPES_INPUT;
        }
        // 新增分类
        Type resultType = this.typeService.saveType(type);
        if (resultType == null) {
            attributes.addFlashAttribute("message", "新增失败！");
        } else {
            attributes.addFlashAttribute("message", "新增成功！");
        }
        return REDIRECT_TYPES;
    }

    /**
     * 修改分类
     *
     * @param type
     * @param bindingResult
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/types/{id}")
    public String editType(@Valid Type type, BindingResult bindingResult, @PathVariable("id") Long id, RedirectAttributes attributes) {
        // 校验名称是否已存在
        Type type1 = this.typeService.getTypeByName(type.getName());
        if (type1 != null) {
            bindingResult.rejectValue("name", "nameError", "该分类已存在，不能重复新增");
        }
        // 校验名称是否为null
        if (bindingResult.hasErrors()) {
            return TYPES_INPUT;
        }
        // 修改分类
        Type resultType = this.typeService.updateType(id, type);
        if (resultType == null) {
            attributes.addFlashAttribute("message", "修改失败");
        } else {
            attributes.addFlashAttribute("message", "修改成功");
        }
        return REDIRECT_TYPES;
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable("id") Long id, RedirectAttributes attributes) {
        this.typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_TYPES;
    }
}
