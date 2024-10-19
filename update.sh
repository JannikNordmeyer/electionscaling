if ! [ -d "qual-o-mat-data" ]; then
  git clone https://github.com/gockelhahn/qual-o-mat-data.git || { echo An error has occured while downloading the Repository.; exit 1; }
fi

cd qual-o-mat-data
git checkout -b temp
git pull https://github.com/gockelhahn/qual-o-mat-data.git || { echo An error has occured while updating the Repository.; git checkout master; git branch --delete temp; exit 1; }
sudo mkdir -p -m=rwx  scaled

cd ..

while :
do
  echo "Enter scaling type (nominal / ordinal). Enter \"exit\" to abort:"
  read scaletype
  if [ $scaletype == "nominal" ] || [ $scaletype == "ordinal" ]; then
    { java -jar builds/uberjar/electionscaling-0.1.0-standalone.jar $scaletype; break; }
  elif [ $scaletype == "exit" ]; then
    break
  else
    echo Invalid scale type.
  fi
done

cd qual-o-mat-data
git checkout master
git merge temp
git branch --delete temp
