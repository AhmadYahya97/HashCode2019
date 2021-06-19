# HashCode 2019 - photo_slideshow
## Task
Given a list of photos and the tags associated with each photo, arrange the photos into
a slideshow that is as interesting as possible (the scoring section below explains what
we mean by “interesting”).

## Problem description
A photo is described by a set of tags.

For example, a photo with a cat on a beach, during a sunny afternoon could be
tagged with the following tags: [cat, beach, sun].

A slideshow is an ordered list of slides. Each slide contains either:

  ● a single horizontal photo, or
  
  ● two vertical photos side-by-side

If the slide contains a single horizontal photo, the tags of the slide are the same as the
tags of the single photo it contains.

For example, a slide containing a single horizontal photo with tags [cat, beach, sun],
has tags [cat, beach, sun].

If the slide contains two vertical photos, the tags of the slide are all the tags present in
any or both of the two photos it contains.

For example, a slide containing two vertical photos with tags [selfie, smile] for the
first photo, and tags [garden, selfie] for the second photo, has tags [selfie, smile,
garden].

Each photo can be used either once or not at all. The slideshow must have at least one
slide.

## Evaluation
The slideshow is scored based on how interesting the transitions between each pair of
subsequent (neighboring) slides are. We want the transitions to have something in
common to preserve continuity (the two slides should not be totally different), but we
also want them to be different enough to keep the audience interested. The similarity
of two vertical photos on a single slide is not taken into account for the scoring
function. This means that two photos can, but don't have to, have tags in common.
For two subsequent slides Si and Si+1, the interest factor is the minimum (the smallest number of the three) of:

  ● the number of common tags between Si and Si+1
  
  ● the number of tags in Si but not in Si+1
  
  ● the number of tags in Si+1 but not in Si
  
## Our Methodology
First, we will search for the image that contains the least number of tags and start from it.

Then if the image is horizontal, we will search for the image that maximizes the score between the two images, otherwise if it's vertical, we will search for the vertical image that maximizes the score between the two images, with a penalization to the image that contains a lot of tags, so that it's left for other images.

### Penalization
Each score will be divided by log(numberOfItsTags) so that the penalization isn't critical for similar values.

## Results
<pre>
File                         Score

A - Example                    2

B - Lovely landscapes          224778

C - Memorable moments          1746

D - Pet pictures               433602

E - Shiny selfies              562089
</pre>
Total score = 1,222,217

HashCodeExtendedRound Rank = 37 worldwide, 1st place in Palestine. 

## Participants
- Ahmad Yahya. ( @AhmadYahya97 )
- Hamed Hijazi. ( @hamed3 )




