import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.Recommender;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.baseline.BaselineScorer;
import org.grouplens.lenskit.baseline.ItemMeanRatingItemScorer;
import org.grouplens.lenskit.baseline.UserMeanBaseline;
import org.grouplens.lenskit.baseline.UserMeanItemScorer;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SimpleFileRatingDAO;
import org.grouplens.lenskit.knn.item.ItemItemGlobalScorer;
import org.grouplens.lenskit.knn.item.ItemItemScorer;
import org.grouplens.lenskit.knn.user.UserUserItemScorer;
import org.grouplens.lenskit.mf.funksvd.FunkSVDItemScorer;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer;
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer;

public class CollExample {
	
	  public static void main(String[] args) {
		String delimiter = ";";
		File inputFile = new File("u.data");
		List<Long> users = new ArrayList(); //playlistsIds
		users.add((long) 2);
		users.add((long) 5);
		
			// We first need to configure the data access.
	        // We will use a simple delimited file; you can use something else like
	        // a database (see JDBCRatingDAO).
	        EventDAO dao = new SimpleFileRatingDAO(inputFile, delimiter);

	        // Second step is to create the LensKit configuration...
	        LenskitConfiguration config = new LenskitConfiguration();
	        // ... configure the data source
	        config.addComponent(dao);
	        // ... and configure the item scorer.  The bind and set methods
	        // are what you use to do that. Here, we want an user-item scorer.
	        
	        
	        /*User-User CF*/
	        config.bind(ItemScorer.class).to(UserUserItemScorer.class);
	        /*Item-Item CF*/
	        //config.bind(ItemScorer.class).to(ItemItemScorer.class);
	        
	        /*FUNK SVD*/
	        //config.bind(ItemScorer.class).to(FunkSVDItemScorer.class);
	        
	        
	        
	        // let's use personalized mean rating as the baseline/fallback predictor.
	        // 2-step process:
	        
	        // First, use the user mean rating as the baseline scorer
	        config.bind(BaselineScorer.class, ItemScorer.class).to(UserMeanItemScorer.class);
	        
	        // Second, use the item mean rating as the base for user means
	        config.bind(UserMeanBaseline.class, ItemScorer.class).to(ItemMeanRatingItemScorer.class);
	        
	        // and normalize ratings by baseline prior to computing similarities
	        config.bind(UserVectorNormalizer.class).to(BaselineSubtractingUserVectorNormalizer.class);

	        
	        
	        // There are more parameters, roles, and components that can be set. See the
	        // JavaDoc for each recommender algorithm for more information.

	        // Now that we have a factory, build a recommender from the configuration
	        // and data source. This will compute the similarity matrix and return a recommender
	        // that uses it.
	        Recommender rec = null;
	        try {
	            rec = LenskitRecommender.build(config);
	        } catch (RecommenderBuildException e) {
	            throw new RuntimeException("recommender build failed", e);
	        }

	        // we want to recommend items
	        ItemRecommender irec = rec.getItemRecommender();
	        assert irec != null; // not null because we configured one
	        
	        
	        // for users
	        for (long user: users) {
	            // get 10 recommendation for the user
	            List<ScoredId> recs = irec.recommend(user, 10);
	            System.out.format("Recommendations for %d:\n", user);
	           
	            for (ScoredId item: recs) {            	
	                System.out.format("\t%d\t%.2f\n", item.getId(), item.getScore());
	            }
	        }
	  }

}
