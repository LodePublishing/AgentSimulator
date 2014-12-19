package com.clawsoftware.agentconfiguration;

/**
 *
 * @author Clemens Lode, clemens at lode.de, 2009, University Karlsruhe (TH), clemens@lode.de
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

class ImageRenderComponent extends JPanel {
	BufferedImage image;
	Dimension size;

	public ImageRenderComponent() {
	}

	public ImageRenderComponent(final BufferedImage image) {
		this.image = image;
		size = new Dimension(image.getWidth(), image.getHeight());
	}

	public void setImage(final BufferedImage image) {
		this.image = image;
		size = new Dimension(image.getWidth(), image.getHeight());
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		// ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		// RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public Dimension getPreferredSize() {
		return size;
	}
}
