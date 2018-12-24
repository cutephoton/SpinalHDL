package spinal.lib.eda
import spinal.core._

/**
 * General purpose component metadata tag used to assist
 * EDA generation classes.
 */
case class ComponentMetaTag (
    displayName     : String        = null,
    author          : String        = null,
    version         : String        = null,
    category        : String        = null,
    description     : String        = null
) extends SpinalTag
