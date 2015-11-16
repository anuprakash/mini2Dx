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

import org.mini2Dx.ui.layout.ScreenSize;
import org.mini2Dx.ui.render.UiRenderer;
import org.mini2Dx.ui.theme.LabelStyle;
import org.mini2Dx.ui.theme.UiTheme;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 *
 */
public class Label extends BasicUiElement<LabelStyle> {
	public static final Color DEFAULT_COLOR = new Color(254f / 255f, 254f / 255f, 254f / 255f, 1f);
	
	private String text;
	private Color color;
	private LabelStyle currentStyle;
	private GlyphLayout glyphLayout;
	
	public Label() {
		this("");
	}
	
	public Label(String text) {
		super();
		color = DEFAULT_COLOR;
		glyphLayout = new GlyphLayout();
		this.text = text;
	}

	@Override
	public void accept(UiRenderer renderer) {
		if(!visible) {
			return;
		}
		renderer.render(this);
	}
	
	@Override
	public void applyStyle(UiTheme theme, ScreenSize screenSize) {
		currentStyle = theme.getLabelStyle(screenSize, styleId);
		glyphLayout.setText(currentStyle.getBitmapFont(), text);
		notifyContentSizeListeners();
	}

	@Override
	public float getContentWidth() {
		return glyphLayout.width;
	}

	@Override
	public float getContentHeight() {
		return glyphLayout.height;
	}

	@Override
	public LabelStyle getCurrentStyle() {
		return currentStyle;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		if(color == null) {
			return;
		}
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if(this.text.equals(text)) {
			return;
		}
		
		this.text = text;
		glyphLayout.setText(currentStyle.getBitmapFont(), text);
		notifyContentSizeListeners();
	}
}
