package com.mastereric.library.common.world.dimension;

import com.google.common.primitives.Ints;
import com.mastereric.library.util.LogUtility;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StructureBookshelfMaze extends StructureStart {
    public StructureBookshelfMaze(World world, Random random, int chunkX, int chunkZ) {
        super(chunkX, chunkZ);
        int centerX = chunkX * 16 + 8;
        int centerZ = chunkZ * 16 + 8;

        StructureBoundingBox boundingBox = createMazeBoundingBox(centerX, centerZ);
        StructureMaze mazeMain = new StructureMaze(boundingBox);

        LogUtility.info("constructor()");

        this.components.add(mazeMain);
        //mazeMain.buildComponent();
        this.updateBoundingBox();
    }

    public static StructureBoundingBox createMazeBoundingBox(int centerX, int centerZ) {
        return new StructureBoundingBox(centerX - 24, 4, centerZ - 24, centerX + 24, 7, centerZ + 24);
    }

    public static class StructureMaze extends StructureComponent {
        private int[][] mazeEdges = null;

        public StructureMaze(StructureBoundingBox box) {
            this.boundingBox = box;
        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound) {
            tagCompound.setShort("width", (short) this.boundingBox.getXSize());
            tagCompound.setShort("height", (short)  this.boundingBox.getZSize());
            tagCompound.setIntArray("data", Ints.concat(mazeEdges));
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            int width = tagCompound.getShort("width");
            int height = tagCompound.getShort("height");
            int[] data = tagCompound.getIntArray("data");
            mazeEdges = new int[width][height];
            for (int x = 0; x < width; x++)
                for (int z = 0; z < height; z++)
                    mazeEdges[x][z] = data[x * width + z];
        }

        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
            if (mazeEdges == null) {
                LogUtility.info("new KruskalGenerator()");
                this.mazeEdges = new KruskalGenerator(mazeEdges.length, mazeEdges[0].length, random).finalMazeEdges;
            }

            LogUtility.info("addComponentParts()");
            for (int x = 0; x < mazeEdges.length; x++) {
                for (int z = 0; z < mazeEdges[0].length; z++) {
                    int xPos, zPos;
                    if (x <= mazeEdges.length / 2) {
                        if (z <= mazeEdges.length / 2) {
                            // Upper-Left Quadrant
                            xPos = structureBoundingBox.minX + x * 2;
                            zPos = structureBoundingBox.minZ + z * 2;
                        } else {
                            xPos = structureBoundingBox.minX + x * 2;
                            zPos = structureBoundingBox.minZ + z * 2 + 1;
                        }
                    } else {
                        if (z <= mazeEdges.length / 2) {
                            // Lower-Left Quadrant
                            xPos = structureBoundingBox.minX + x * 2 + 1;
                            zPos = structureBoundingBox.minZ + z * 2;
                        } else {
                            xPos = structureBoundingBox.minX + x * 2 + 1;
                            zPos = structureBoundingBox.minZ + z * 2 + 1;
                        }
                    }

                    for (int xi = 0; xi < 2; xi++) {
                        for (int zi = 0; zi < 2; zi++) {
                            if (xi == 0 && zi == 0) {
                                // The northwest corner is always filled
                                fillColumn(world, xPos + xi, structureBoundingBox.minY, structureBoundingBox.maxY, zPos + zi, Blocks.BOOKSHELF);
                            } else if (xi == 1 && zi == 0) {
                                // The north edge.
                                if ((mazeEdges[x][z] & KruskallEdge.NORTH) == 0) {
                                    fillColumn(world, xPos + xi, structureBoundingBox.minY, structureBoundingBox.maxY, zPos + zi, Blocks.BOOKSHELF);
                                }
                            } else if (xi == 0 && zi == 1) {
                                // The west edge.
                                if ((mazeEdges[x][z] & KruskallEdge.WEST) == 0) {
                                    fillColumn(world, xPos + xi, structureBoundingBox.minY, structureBoundingBox.maxY, zPos + zi, Blocks.BOOKSHELF);
                                }
                            }
                            // The southeast corner of each cells is never filled.
                        }
                    }
                }
            }

            // Vines
            for (int x = structureBoundingBox.minX; x < structureBoundingBox.maxX; x++) {
                for (int z = structureBoundingBox.minZ; z < structureBoundingBox.maxZ; z++) {
                    if(random.nextInt(32) == 0) {
                        setBlockWithDefaultState(world, x, structureBoundingBox.minY + 1, x, Blocks.VINE);
                    }
                }
            }
            return true;
        }

        private void fillColumn(World world, int xPos, int yMin, int yMax, int zPos, Block block) {
            for (int y = yMin; y < yMax; y++)
                setBlockWithDefaultState(world, xPos, y, zPos, block);
        }

        private void setBlockWithDefaultState(World world, int xPos, int yPos, int zPos, Block block) {
            setBlock(world, xPos, yPos, zPos, block.getDefaultState());
        }

        private void setBlock(World world, int xPos, int yPos, int zPos, IBlockState block) {
            world.setBlockState(new BlockPos(xPos, yPos, zPos), block);
        }
    }

    public static class KruskalGenerator {
        private final int width;
        private final int height;
        private final Random random;

        public KruskalGenerator(int width, int height, Random random) {
            this.width = width;
            this.height = height;
            this.random = random;
            initialize();
        }

        private List<List<KruskalTree>> treeArray;
        private List<KruskallEdge> edgeArray;
        // Array of shorts, with each number
        public int[][] finalMazeEdges;

        private void initialize() {
            finalMazeEdges = new int[width][height];

            treeArray = new ArrayList<List<KruskalTree>>();
            for (int x = 0; x < width; x++) {
                List<KruskalTree> tempRowArray = new ArrayList<KruskalTree>();
                for (int y = 0; y < height; y++) {
                    tempRowArray.add(new KruskalTree());
                }
                treeArray.add(tempRowArray);
            }

            edgeArray = new ArrayList<KruskallEdge>();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // Add the north and west edges. The east and south edges are the edges of the neighbors.
                    edgeArray.add(new KruskallEdge(x, y, KruskallEdge.NORTH));
                    edgeArray.add(new KruskallEdge(x, y, KruskallEdge.EAST));
                }
            }

            randomizeEdges();

            generateMaze();
        }

        private void generateMaze() {
            while (edgeArray.size() > 0) {
                KruskallEdge currentEdge = edgeArray.remove(0);
                int currentX = currentEdge.x;
                int currentY = currentEdge.y;
                int currentDir = currentEdge.direction;

                KruskalTree currentTreeA;
                KruskalTree currentTreeB;

                if (currentDir == KruskallEdge.NORTH) {
                    currentTreeA = treeArray.get(currentX).get(currentY);
                    currentTreeB = treeArray.get(currentX).get(currentY - 1);
                    finalMazeEdges[currentX][currentY] |= KruskallEdge.NORTH;
                    finalMazeEdges[currentX][currentY-1] |= KruskallEdge.SOUTH;
                } else {
                    currentTreeA = treeArray.get(currentX).get(currentY);
                    currentTreeB = treeArray.get(currentX + 1).get(currentY);
                    finalMazeEdges[currentX][currentY] |= KruskallEdge.EAST;
                    finalMazeEdges[currentX+1][currentY] |= KruskallEdge.WEST;
                }
                // If the trees are not connect them, connect them and "destroy" the wall between them,
                // by assigning the edge as false. If they are already connected, keep the wall.
            }
        }

        private void randomizeEdges() {
            Collections.shuffle(edgeArray, random);
        }
    }

    private static class KruskalTree {

        private KruskalTree parent = null;

        public KruskalTree() {
        }

        public KruskalTree getRoot() {
            return (parent != null) ? parent.getRoot() : this;
        }

        public boolean isConnected(KruskalTree tree) {
            return this.getRoot() == tree.getRoot();
        }

        public void connectToTree(KruskalTree tree) {
            tree.getRoot().setParent(this);
        }

        private void setParent(KruskalTree parent) {
            this.parent = parent;
        }
    }

    private static class KruskallEdge {
        static final int NORTH = 1;
        static final int EAST = 2;
        static final int SOUTH = 4;
        static final int WEST = 8;

        final int x;
        final int y;
        final int direction;
        public KruskallEdge(int x, int y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
    }
}
