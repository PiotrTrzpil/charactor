package charactor.core.model.persistance

class WorldMapSnapshot
{
	var charactorsState: List[CharactorSnapshot] = List();
	var foodSourcesState: List[FoodSourceSnapshot] = List();
	var message: String = null;
}
