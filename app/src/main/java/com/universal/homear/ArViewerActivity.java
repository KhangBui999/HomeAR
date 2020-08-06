package com.universal.homear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * This activity allows a user to utilise the AR feature. The code below handles 3D model rendering
 * using Google Sceneform, an API that reduces the boilerplate code needed to render AR. Moreover,
 * the code also handles retrieving 3D objects from the Firebase server.
 */
public class ArViewerActivity extends AppCompatActivity {

    private ArFragment fragment;
    private Button mClear, mHelp;
    private ImageView mBack;
    private FloatingActionButton mCamera;

    private ModelRenderable renderable;
    private Context context;
    private int duration;
    private AnchorNode anchorNode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_viewer);

        context = getApplicationContext();
        duration = Toast.LENGTH_SHORT;

        mClear = findViewById(R.id.btn_clear);
        mClear.setOnClickListener(v -> clearObject());

        //Launches a help dialog for the user. Provides quickstart instructions.
        mHelp = findViewById(R.id.btn_help);
        mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArViewerHelpDialog dialog = new ArViewerHelpDialog();
                dialog.show(getSupportFragmentManager(), "com.universal.homear.ArViewerActivity");
            }
        });

        //Back button
        mBack = findViewById(R.id.iv_backBtn);
        mBack.setOnClickListener(v -> finish());

        //Disabled feature - camera
        mCamera = findViewById(R.id.fab_picture);
        mCamera.setVisibility(View.INVISIBLE);

        //Retrieves objectFileId from the Furniture Detail screen
        Intent intent = getIntent();
        String fileId = intent.getStringExtra("objectFileId") + ".glb";

        //Initialises the firebase storage
        FirebaseApp.initializeApp(this);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference modelRef = storage.getReference().child(fileId); //change this to intent pass-by value
        fragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        try {
            File file = File.createTempFile(fileId.substring(0, fileId.indexOf('.')), "glb");
            modelRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Builds the model object to be used in the AR fragment
                    buildModel(file);
                    //Handles user interaction with the AR fragment
                    onTapListenerHandler();
                    //User prompt to tap on the screen
                    Toast toast = Toast.makeText(context, "Tap the screen to place object!", duration);
                    toast.show();
                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds the 3D Model with Google Sceneform. The API allows for accurate measurements of
     * the surrounding environment and renders the object with shadow and lighting effects.
     * @param file
     */
    private void buildModel(File file) {
        //Renders file from the Firebase Storage to a Sceneform compatible object model
        RenderableSource source = RenderableSource
                .builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();
        //Builds the compatible object into an AR model
        ModelRenderable
                .builder()
                .setSource(this, source)
                .setRegistryId(file.getPath())
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;
                });
    }

    /**
     * Allows the user to place an AR model.
     */
    private void onTapListenerHandler() {
        fragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            //Creates basic anchorNode shell
            anchorNode = new AnchorNode(hitResult.createAnchor());
            //Creates a transformable node ensuring users can resize, rotate and move the object
            TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
            //Set current rendered model to be the resultant AR object
            node.setRenderable(renderable);
            //Links transformable node to anchorNode
            node.setParent(anchorNode);
            //Add AR object to the scene
            fragment.getArSceneView().getScene().addChild(anchorNode);
            //Selects node to enable users to manipulate the object
            node.select();
            //Toast to display status
            Toast toast = Toast.makeText(context, "Object placed!", duration);
            toast.show();
            //Prevents users from adding more than 1 object
            fragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
                @Override
                public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                    Toast toast = Toast.makeText(context, "Please clear current object!", duration);
                    toast.show();
                }
            });
        }));
    }

    /**
     * Allows a user to clear the current object from their view.
     */
    private void clearObject() {
        try{
            fragment.getArSceneView().getScene().removeChild(anchorNode);
            onTapListenerHandler();
            Toast toast = Toast.makeText(context, "Object removed!", duration);
            toast.show();
        }
        catch(Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(context, "No object detected!", duration);
            toast.show();
        }
    }

}