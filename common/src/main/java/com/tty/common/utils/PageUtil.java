package com.tty.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @author caoguirong
 */
@Data
public class PageUtil implements Serializable {

	/**
	 * 总记录数
	 */
	private int total;

	/**
	 * 每页记录数
	 */
	private int size = 10;

	/**
	 * 当前页数
	 */
	private int current = 1;

	/**
	 * 总页数
	 */
	private int pages;

	/**
	 * 列表数据
	 */
	private List<?> list;

	/**
	 * 总选择数
	 */
	private Integer allChoice;

	/**
	 * 分页
	 * @param list        列表数据
	 * @param total  总记录数
	 * @param size    每页记录数
	 * @param current    当前页数
	 */
	public PageUtil(List<?> list, int total, int size, int current) {
		this.list = list;
		this.total = total;
		this.size = size;
		this.current = current;
		this.pages = (int) Math.ceil((double) total / size);
	}

	/**
	 * 分页
	 */
	public PageUtil(IPage<?> page) {
		this.list = page.getRecords();
		this.total = (int)page.getTotal();
		this.size = (int)page.getSize();
		this.current = (int)page.getCurrent();
		this.pages = (int)page.getPages();
	}

	/**
	 * 分页
	 */
	public PageUtil(PageInfo<?> pageInfo) {
		this.list = pageInfo.getList();
		this.total = (int)pageInfo.getTotal();
		this.size = pageInfo.getPageSize();
		this.current = pageInfo.getPageNum();
		this.pages = pageInfo.getPages();
	}

	public PageUtil() {
	}
}
