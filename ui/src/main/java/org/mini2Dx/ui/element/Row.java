/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ui.element;

import java.util.ArrayList;
import java.util.List;

import org.mini2Dx.core.engine.SizeChangeListener;
import org.mini2Dx.core.engine.geom.CollisionBox;
import org.mini2Dx.core.exception.MdxException;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.UiElement;
import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.render.UiRenderer;
import org.mini2Dx.ui.theme.NullStyle;
import org.mini2Dx.ui.theme.UiTheme;

/**
 *
 */
public class Row extends BasicUiElement<NullStyle> implements SizeChangeListener<CollisionBox> {
	protected final List<UiElement<?>> children = new ArrayList<UiElement<?>>(1);

	private float contentWidth, contentHeight;
	private boolean childAdded = false;

	public Row() {
		super();
		super.setXRules("xs-0");
		super.setWidthRules("xs-12");
	}

	public Row(String id) {
		super(id);
		super.setXRules("xs-0");
		super.setWidthRules("xs-12");
	}

	@Override
	public void update(UiContainer uiContainer, float delta) {
		if(childAdded) {
			calculateContentDimensions();
			childAdded = false;
		}
		
		super.update(uiContainer, delta);
		boolean childRemoved = false;
		
		for (int i = 0; i < children.size(); i++) {
			UiElement<?> element = children.get(i);
			if (element.disposed()) {
				children.remove(i);
				i--;
				childRemoved = true;
			} else {
				element.update(uiContainer, delta);
			}
		}
		
		if(!childRemoved) {
			return;
		}
		calculateContentDimensions();
	}

	@Override
	public void resize(ScreenSize screenSize, UiTheme theme, float columnWidth, float totalHeight) {
		super.resize(screenSize, theme, columnWidth, totalHeight);

		for (int i = 0; i < children.size(); i++) {
			children.get(i).resize(screenSize, theme, columnWidth, totalHeight);
		}
	}

	@Override
	public UiElement<?> getById(String id) {
		if (id.equals(getId())) {
			return this;
		}
		for (UiElement<?> element : children) {
			UiElement<?> result = element.getById(id);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	public void accept(UiRenderer renderer) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).accept(renderer);
		}
	}

	@Override
	public void applyStyle(UiTheme theme, ScreenSize screenSize) {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).applyStyle(theme, screenSize);
		}
	}

	public void addChild(UiElement<?> element) {
		element.addSizeChangeListener(this);
		currentArea.addPostionChangeListener(element);
		children.add(element);
		childAdded = true;
	}

	public void removeChild(UiElement<?> element) {
		element.dispose();
	}

	@Override
	public void setXRules(String rules) {
		throw new MdxException("Cannot set x rules on " + Row.class.getSimpleName());
	}

	@Override
	public void setWidthRules(String rules) {
		throw new MdxException("Cannot set width rules on " + Row.class.getSimpleName());
	}

	@Override
	public void sizeChanged(CollisionBox child) {
		calculateContentDimensions();
	}

	@Override
	public float getContentWidth() {
		return contentWidth;
	}

	@Override
	public float getContentHeight() {
		return contentHeight;
	}
	
	private void calculateContentDimensions() {
		float contentWidth = 0f;
		float contentHeight = 0f;
		
		for (UiElement<?> element : children) {
			contentWidth += element.getContentWidth();
			contentHeight = Math.max(contentHeight, element.getContentHeight());
		}
		
		this.contentWidth = contentWidth;
		this.contentHeight = contentHeight;
	}
	
	public static Row withElements(UiElement<?>... elements) {
		Row row = new Row();
		for (UiElement<?> element : elements) {
			row.addChild(element);
		}
		return row;
	}

	public static Row withElements(String rowId, UiElement<?>... elements) {
		Row row = new Row(rowId);
		for (UiElement<?> element : elements) {
			row.addChild(element);
		}
		return row;
	}

	@Override
	public NullStyle getCurrentStyle() {
		return NullStyle.INSTANCE;
	}
}