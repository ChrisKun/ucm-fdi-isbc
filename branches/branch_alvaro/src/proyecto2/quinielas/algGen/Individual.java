package quinielas.algGen;
/**
 * An individual of a GA population. Knows how to mute and combine itself with other individual
 * @author Juan A. Recio
 */
public interface Individual {

	public void mutation();
	
	public Individual combination(Individual other);
}
