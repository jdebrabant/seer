#include <stdio.h>
#include <stdlib.h>
#pragma pack(2)

const int WIDTH = 2048; 
const int HEIGHT = 1216; 


typedef struct
{
    unsigned char blue;
    unsigned char green;
    unsigned char red;
} Rgb;


int main( int argc, char **argv ) {

        FILE *inFile;
        FILE *outFile; 
        
        if(argc != 3)
        {
        	printf("Usage: convertImage <input file> <output file>\n"); 
        	return -1; 
        }

        printf( "Opening file %s for reading.\n", argv[1] );

        inFile = fopen( argv[1], "rb" );
        if( !inFile ) {
                printf( "Error opening file %s.\n", argv[1] );
            return -1;
        }

        printf( "Opening file %s for writing.\n", argv[2] );
        outFile = fopen( argv[2], "w" );
        
        if( !outFile ) {
                printf( "Error opening outputfile.\n" );
                return -1;
        }
        
        
        Rgb *pixel = (Rgb*) malloc( sizeof(Rgb) );
        int read; 
        int i = 0, j = 0; 
        
        printf("converting image..."); 
        for(j = 0; j < HEIGHT; j++ ) 
        {
                //printf( "------ Row %d\n", j+1 );
                read = 0;
                for(i = 0; i < WIDTH; i++ ) {
                        if( fread(pixel, 1, sizeof(Rgb), inFile) != sizeof(Rgb) ) 
                        {
                                printf( "Error reading pixel!\n" );
                                return -1;
                        }
                        read += sizeof(Rgb);
                        fprintf(outFile, "%d %d %d ", pixel->red, pixel->green, pixel->blue);
                        //printf( "Pixel (%d, %d): %3d %3d %3d\n", j, i, pixel->red, pixel->green, pixel->blue );
                }
                
                //fprintf(outFile, "\n"); 
                if( read % 4 != 0 ) 
                {
                        read = 4 - (read%4);
                        printf( "Padding: %d bytes\n", read );
                        fread( pixel, read, 1, inFile );
                }
        }

        printf( "done.\n" );
        fclose(inFile);
        fclose(outFile);

        return 0;

}

