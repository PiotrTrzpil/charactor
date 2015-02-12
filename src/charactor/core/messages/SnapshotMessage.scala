package charactor.core.messages

import charactor.core.model.persistance._

class SnapshotMessage(val charactorsState: List[CharactorSnapshot], val foodSourcesState: List[FoodSourceSnapshot])
{
}
