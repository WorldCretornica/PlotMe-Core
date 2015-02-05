PlotMe-Core
===========
Bukkit Plugin for managing plots. The plugin is able to operate on worlds which are generated using a class that implement the IPlotMe_ChunkGenerator. PlotMe-DefaultGenerator is the first plugin that implement that generator.

Dev Builds: http://ci.worldcretornica.com:8080/

Other plugins can be created into which PlotMe-Core can interact. Look at the PlotMe-DefaultGenerator source to see how it can be done.

I intend on creating in the near future the following plugin generators, but feel free to create one and I will create a link on the PlotMe page to your plugin.
- Flat world generator with no roads, every plot is connected without anything visible. Option to have the plot world a different block but still allow it to be modified by the plot owner.
- Normal world generator which does not customize blocks and no roads.

Ideas for other generators but that I will not create :
- Islands
- Space
- Cave
