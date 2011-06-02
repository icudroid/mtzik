import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;


public class Test {
	public static void main(String[] args) throws IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException, CannotWriteException {
		
		File file = new File("/home/dimitri/tmp/test51.mp3");

		MP3File f      = (MP3File)AudioFileIO.read(file);
		Tag tag = f.getID3v2Tag();
		
		
		Artwork firstArtwork = tag.getFirstArtwork();
		BufferedImage image = firstArtwork.getImage();
				
		BufferedImage i1 = new BufferedImage(image.getWidth(), image.getHeight()+150, image.getType());
		Graphics graphics = i1.getGraphics();
		graphics.drawImage(image, 0, 0,image.getWidth(), image.getHeight(), 0, 0,image.getWidth(), image.getHeight(), null);
		
		graphics.setColor(Color.RED);
		graphics.setFont(new Font("Arial", Font.BOLD, 12));
		graphics.drawString("Test", 10, image.getHeight()+16);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(i1,"png",baos);
		byte[] buffer = baos.toByteArray();

		Artwork a = new Artwork();
		a.setBinaryData(buffer);
		a.setDescription(firstArtwork.getDescription());
		a.setMimeType(firstArtwork.getMimeType());
		a.setPictureType(firstArtwork.getPictureType());
		
		tag.deleteArtworkField();
		
		tag.addField(a);
		
		/*Artwork createArtworkFromFile = Artwork.createArtworkFromFile(new File("/home/dimitri/tmp/coverart.png"));
		tag.deleteArtworkField();
		tag.addField(createArtworkFromFile);*/
		
		
		
		f.commit();
	}
}

