package com.universal.homear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

public class ArViewerActivity extends AppCompatActivity {

    private ArFragment fragment;
    private Button mClear;

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

        //Retrieves objectFileId from the Furniture Detail screen
        Intent intent = getIntent();
        String fileId = intent.getStringExtra("objectFileId");
        String dummyId = "default.glb"; //replace with fileId

        //Initialises the firebase storage
        FirebaseApp.initializeApp(this);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference modelRef = storage.getReference().child(dummyId); //change this to intent pass-by value
        fragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        try {
            File file = File.createTempFile(dummyId.substring(0, dummyId.indexOf('.')), "glb");
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
     *
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
     *
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
     *
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